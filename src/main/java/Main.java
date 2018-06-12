import jdk.nashorn.internal.parser.JSONParser;
import org.json.*;
import org.json.JSONString;
import spoon.Launcher;
import spoon.processing.Processor;
import spoon.support.gui.SpoonModelTree;

import java.util.*;


public class Main {

    public static void main(String[] args) throws Exception
    {

        //Not Sniper  Mode
		Launcher launcher = new Launcher();
		launcher.addInputResource("./source/act/");
        launcher.getEnvironment().setCommentEnabled(true);
        launcher.getEnvironment().setAutoImports(true);
//        launcher.getEnvironment().
        String projectKey="fr.inria.gforge.spoon:spoon-core";
		launcher.addProcessor((Processor) new SerializableFieldProcessor(projectKey));
		launcher.run();
//	   	new SpoonModelTree(launcher.getFactory());

        /*
        //Sniper Mode . Add pavel's refDJPP branch of spoon as library to use this.
        Launcher launcher = new Launcher();
        launcher.addInputResource("/home/ashutosh/TBCH/act");
        launcher.getEnvironment().setCommentEnabled(true);
        launcher.getEnvironment().setAutoImports(true);
        launcher.buildModel();
        Factory f = launcher.getFactory();
        new SourceFragmentsTreeCreatingChangeCollector().attachTo(f.getEnvironment());

        CtClass<?> ctClass = launcher.getFactory().Class().get(ReferenceBuilder.class);
//        SniperHelper.process(ctClass);

        ChangesAwareDefaultJavaPrettyPrinter printer = new ChangesAwareDefaultJavaPrettyPrinter(f.getEnvironment());
        CompilationUnit cu = f.CompilationUnit().getOrCreate(ctClass);
        List<CtType<?>> toBePrinted = new ArrayList<>();
        toBePrinted.add(ctClass);
        printer.calculate(cu, toBePrinted);
        */
        System.out.println("done");
	}
}