package com.e5.annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes({"com.e5.annotationcreation.annotations.BaseAbsClassBuilder"})
@AutoService(Processor.class)
public class BaseAbsClassAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process (Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            if (((BaseAbsClassBuilder)annotation).classType1() == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@BaseAbsClassBuilder must be applied  with a ClassType1 argument", annotation);
            }
            if (((BaseAbsClassBuilder)annotation).classType2() == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@BaseAbsClassBuilder must be applied  with a ClassType2 argument", annotation);
            }

            String className = ((TypeElement) annotation.getEnclosingElement()).getQualifiedName().toString();

            try {
                writeBuilderFile(className, annotation);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    private void writeBuilderFile(String className, TypeElement annotation) throws IOException {

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

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();


            out.print("    private ");
            out.print(simpleClassName);
            out.print(" object = new ");
            out.print(simpleClassName);
            out.println("(){};"); // abstract class implementation
            out.println();

            out.print("    public ");
            out.print(simpleClassName);
            out.println(" build() {");
            out.println("        object.setParameter1("+ ((BaseAbsClassBuilder)  annotation).classType1()+");");
            out.println("        object.setParameter2("+ ((BaseAbsClassBuilder)  annotation).classType2()+");");
            out.println("        object.setParameter3("+ ((BaseAbsClassBuilder)  annotation).flag1()+");");
            out.println("        object.setParameter4("+ ((BaseAbsClassBuilder)  annotation).flag2()+");");
            out.println("        return object;");
            out.println("    }");
            out.println();

            out.println("}");

        }
    }
}
