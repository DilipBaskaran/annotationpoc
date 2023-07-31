package com.e5.annotation.processor;

import com.e5.annotation.services.SuperAbsClass;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.e5.annotation.processor.BaseAbsClassBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class BaseAbsClassAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process (Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            System.out.println("annotation -----" + annotation);
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                System.out.println("element--->"+ element);
                // Get the annotation element from the type element
                Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = null;
                List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
                for (AnnotationMirror annotationMirror : annotationMirrors) {
                    System.out.println("annotation Mirror--->"+ annotationMirror);
                    elementValues = annotationMirror.getElementValues();
                    System.out.println("element values--->"+ elementValues);

                        /*if (classType1 == null) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@BaseAbsClassBuilder must be applied  with a ClassType1 argument", annotation);
                        }
                        if (classType2 == null) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@BaseAbsClassBuilder must be applied  with a ClassType2 argument", annotation);
                        }*/
                }

                if (elementValues != null && !elementValues.isEmpty()) {
                    String className = ((TypeElement) element).getQualifiedName().toString();
                    try {
                        writeBuilderFile(className, elementValues);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        //testing
        //getAbstractMethodImplementations(SuperAbsClass.class);

        return true;
    }

    private void appendAbstractMethodImplementations (Class absClass,
                                                     PrintWriter out,
                                                     Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues) {

        StringBuilder absMethodImp = new StringBuilder();

        out.println(" {");
        Method[] classMethods = absClass.getDeclaredMethods();
        for (Method method : classMethods) {
            int methodModifier = method.getModifiers();

            if (Modifier.isAbstract(methodModifier)) {
                System.out.println("Abstract method block");
                switch (method.getName()) {
                    case "method":
                        out.println("        @Override");
                        out.print("        "+ (Modifier.isPublic(methodModifier)? "public ": "protected ") + method.getAnnotatedReturnType() + " " + method.getName() + " (");
                        out.println(Arrays.stream(method.getParameters())
                                .map(parameter -> parameter.getParameterizedType().getTypeName() + " " + parameter.getName())
                                .collect(Collectors.joining(", ")) + ") {");
                        System.out.println("Abstract method block method..." + method.getName());
                        System.out.println(elementValues.entrySet()
                                .stream()
                                .filter(entry -> entry.getKey().getSimpleName().toString().equals(method.getName())).count());
                        Optional<? extends Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> paramValueOpt = elementValues.entrySet()
                                .stream()
                                .filter(entry -> entry.getKey().getSimpleName().toString().equals(method.getName()))
                                .findFirst();
                        String returnStr = paramValueOpt.isPresent() ? paramValueOpt.get().getValue().getValue().toString() : null;
                        System.out.println(returnStr);
                        out.println("            return " + "java.util.Arrays.asList(" + returnStr + ");");
                        out.println("        }");
                        break;
                }

            }
        }
        out.print("    };");

    }

    private void writeBuilderFile (String className,
                                   Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues) throws IOException {

        System.out.println("ClassName-----" + className);
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot); // to get  Simple ClassName from the qualified name of the class
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "Builder";
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            //Package
            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            //Super Class import
            out.print("import ");
            out.print(SuperAbsClass.class.getName());
            out.println(";");

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();


            out.print("    private static ");
            out.print(SuperAbsClass.class.getSimpleName());
            out.print(" object = new ");
            out.print(SuperAbsClass.class.getSimpleName());
            out.print("()"); // abstract class implementation
            System.out.println("testing error---------");
            appendAbstractMethodImplementations(SuperAbsClass.class, out, elementValues);
            out.println();

            out.print("    public static ");
            out.print(SuperAbsClass.class.getSimpleName());
            out.println(" build() {");
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                    : elementValues.entrySet()) {
                String key = entry.getKey().getSimpleName().toString();
                Object value = entry.getValue().getValue();
                System.out.println(key + " ---- " + entry.getValue() + " ----- " + value);
                switch (key) {
                    case "flag1":
                    case "classType1":
                    case "integerValue":
                    case "classLevel2Type":
                        out.println("        object.set"
                                + Character.toUpperCase(key.charAt(0)) + key.substring(1)
                                + "(" + entry.getValue() + ");");
                        break;
                    case "classLevel2Types":
                        out.println("        object.set"
                                + Character.toUpperCase(key.charAt(0)) + key.substring(1)
                                + "(new Class[]" + entry.getValue() + ");");
                        break;
                    case "classLevel1Types":
                        out.println("        object.set"
                                + Character.toUpperCase(key.charAt(0)) + key.substring(1)
                                + "(java.util.Arrays.asList(" + entry.getValue().getValue().toString() + "));");
                        break;
                    /*case "stringValue":
                        String strVal = (String) value;
                        System.out.printf(">> stringValue: %s\n", strVal);
                        break;
                    case "enumValue":
                        VariableElement enumVal = ((VariableElement) value);
                        System.out.printf(">> enumValue: %s\n", enumVal.getSimpleName());
                        break;
                    case "annotationTypeValue":
                        AnnotationMirror anoTypeVal = (AnnotationMirror) value;
                        System.out.printf(">> annotationTypeValue: %s\n", anoTypeVal.toString());
                        break;
                    case "classValue":
                        TypeMirror typeMirror1 = (TypeMirror) value;
                        System.out.printf(">> classValue: %s\n", typeMirror1.toString());
                        break;
                    case "classesValue":
                        List<? extends AnnotationValue> typeMirrors
                                = (List<? extends AnnotationValue>) value;
                        System.out.printf(">> classesValue: %s\n",
                                ((TypeMirror) typeMirrors.get(0).getValue()).toString());
                        break;*/
                }
            }
            out.println("        return object;");
            out.println("    }");
            out.println();

            out.println("}");
        }
    }
}
