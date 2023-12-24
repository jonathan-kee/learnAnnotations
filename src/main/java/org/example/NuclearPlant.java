package org.example;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.System.out;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@interface Pending{}

@Retention(RetentionPolicy.RUNTIME) // basically without retention, code will not work
@interface TaskInfo{
    public abstract TaskPriority priority() default TaskPriority.HIGH;  // Required
    public abstract String taskDesc(); // Optional
    public abstract String[] assignedTo();  // Optional
    public enum TaskPriority{ LOW, MED, HIGH}
}

@Pending
@TaskInfo(
        priority   = TaskInfo.TaskPriority.HIGH,
        taskDesc   = "Class for running a nuclear reactor.",
        assignedTo = { "Tom", "Dick", "Harriet" }
)
public class NuclearPlant {

    @Pending
    public NuclearPlant() {
    }

    @Deprecated(forRemoval = true, since = "8")
    public boolean outOfProduction;

    @Deprecated(since = "10")
    public void notInUse() {
    }

    @Pending
    @TaskInfo(
            taskDesc = "Procedure for nuclear reactor shutdown",
            assignedTo = { "Tom", "Harriet" }
    )
    public void shutDownNuclearReactor() {
    }

    @TaskInfo(
            priority = TaskInfo.TaskPriority.LOW,
            taskDesc = "Exchange nuclear rods",
            assignedTo = { "Tom", "Dick" }
    )
    public void changeNuclearRods() {
    }

    @TaskInfo(
            priority = TaskInfo.TaskPriority.LOW,
            taskDesc = "Adjust nuclear fuel",
            assignedTo = { "Harriet" }
    )
    public class Main {
        public static void main(String[] args) {
            out.println("Hello world!");
        }
    }
}

class TaskInfoAnnotationProcessor {
    public static void printTaskInfoAnnotation(AnnotatedElement... elements) {                                // (1)
        Stream.of(elements)                                                                                   // (2)
                .filter(annotatedElement -> annotatedElement.isAnnotationPresent(TaskInfo.class))             // (3)
                .peek(annotatedElement ->
                        out.printf("%s annotation for '%s':%n", TaskInfo.class.getName(), annotatedElement))  // (4)
                .flatMap(annotatedElement -> Stream.of(
                        annotatedElement.getDeclaredAnnotationsByType(TaskInfo.class)))                       // (5)
                .forEach(annotation -> {                                                                               // (6)
                    out.printf("  Task   description: %s%n", annotation.taskDesc());
                    out.printf("  Priority: %s%n", annotation.priority());
                    out.printf("  Assigned to: %s%n", Arrays.toString(annotation.assignedTo()));
                });
    }

    public static void main(String[] args) {
        Class<?> classobj = NuclearPlant.class;                                 // (7)
        printTaskInfoAnnotation(classobj);                                      // (8)
        printTaskInfoAnnotation(classobj.getDeclaredMethods());                 // (9)
    }
}

class AnnotationPrinter {
    public static void printAllAnnotations(Class<?> classobj) {                // (1)
        printAnnotatedElements(classobj);                                      // (2)
        printAnnotatedElements(classobj.getDeclaredConstructors());            // (3)
        printAnnotatedElements(classobj.getDeclaredMethods());                 // (4)
        printAnnotatedElements(classobj.getDeclaredFields());                  // (5)
    }

    public static void printAnnotatedElements(AnnotatedElement... elements) {// (6)
        Stream.of(elements)                                                    // (7)
                .peek(annotatedElement -> System.out.printf("Annotations for \'%s\':%n", annotatedElement))  // (8)
                .flatMap(annotatedElement -> Stream.of(annotatedElement.getDeclaredAnnotations()))           // (9)
                .forEach(annotation -> System.out.println("  " + annotation));                               // (10)
    }
}

class AnnotationClient {
    public static void main(String[] args) {
        AnnotationPrinter.printAllAnnotations(NuclearPlant.class);
    }
}