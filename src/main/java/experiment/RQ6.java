package experiment;

import Main.MethodInitialization;
import config.Constant;
import entity.PatchFile;
import entity.Subject;
import lombok.extern.slf4j.Slf4j;
import run.Runner;

import java.util.List;
@Slf4j
public class RQ6 {
    public static void main(String[] args){
        String project = "Math";
        int start = 53;
        int end = 53;

        boolean modificationMethod = true;
        boolean variable = true;
        boolean trace = true;
        int queryStrategy =0;
        for (String arg : args) {
            if (arg.startsWith("-p=")) {
                // Constant.PROJECT_HOME = args[i].substring("--proj_home=".length());
                project = arg.substring("-p=".length());
            } else if (arg.startsWith("-s=")) {
                start = Integer.parseInt(arg.substring("-s=".length()));
            } else if (arg.startsWith("-e=")) {
                end = Integer.parseInt(arg.substring("-e=".length()));
            } else if (arg.startsWith("-t=")){
                if(arg.substring("-t=".length()).equalsIgnoreCase("method")){
                    modificationMethod = false;
                }else  if (arg.substring("-t=".length()).equalsIgnoreCase("variable")){
                    variable = false;
                }else if(arg.substring("-t=".length()).equalsIgnoreCase("trace")){
                    trace = false;
                }
            } else if(arg.startsWith("-stra=")){
                queryStrategy = Integer.parseInt(arg.substring("-stra=".length()));
            }
        }

        double [] errors = {0.02, 0.04, 0.06, 0.08, 0.1};
        for(double error : errors){
            log.info("Error: " + error);
            for(int times = 0; times < 10; times++){
                log.info("Times: " + times + " Start Running <<" + project +">> AllQuery from " + start + " to " + end );
                for (int i = start; i <= end; i++) {
                    Subject subject = new Subject(project, i);
                    if (subject.initPatchListByPath(Constant.AllPatchPath)) {
                        log.info("Process " + subject.toString());

                        List<PatchFile> patchList = subject.getPatchList();
                        if(!subject.exist()){
                            if(! Runner.downloadSubject(subject)){
                                continue;
                            }
                        }
                        AllQuery allQuery = new AllQuery(subject);
                        MethodInitialization methodInitialization = new MethodInitialization(subject);
                        methodInitialization.MainProcess();
                        allQuery.queryProcessByError(error);

                    }
                }
            }
        }


    }
}