import org.json.JSONArray;
import org.json.JSONException;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import java.util.HashSet;
import java.util.Set;

public class SerializableFieldProcessor extends AbstractProcessor<CtField> {

    private JSONArray jsonArray;//array of JSONObjects, each of which is a bug
    private Set<Bug> SetOfBugs;//set of bugs, corresponding to jsonArray
    private Set<Long> SetOfLineNumbers;//set of line numbers corresponding to bugs, just for efficiency
    private Set<String> SetOfFileNames;//-----
    private Bug thisBug;               //current bug. This is set inside isToBeProcessed function
    private String thisBugName;        //name (message) of current thisBug.
    public SerializableFieldProcessor(String projectKey) throws Exception {
        jsonArray= ParseAPI.parse(1948,"",projectKey);
        SetOfBugs = Bug.createSetOfBugs(this.jsonArray);
        SetOfLineNumbers=new HashSet<Long>();
        SetOfFileNames=new HashSet<String>();
        thisBug=new Bug();
        for(Bug bug:SetOfBugs)
        {
            SetOfLineNumbers.add(bug.getLineNumber());
            SetOfFileNames.add(bug.getFileName());
        }
    }


    @Override
    public boolean isToBeProcessed(CtField element)
    {
        if(element==null)
        {
            return false;
        }
        long line =-1;
        String targetName="",fileOfElement="";
        line=(long) element.getPosition().getLine();
        String split1[]=element.getPosition().getFile().toString().split("/");
        fileOfElement=split1[split1.length-1];
        targetName=element.getSimpleName();
        if(!SetOfLineNumbers.contains(line)||!SetOfFileNames.contains(fileOfElement))
        {
            return false;
        }
        try {
            thisBug = new Bug();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Bug bug:SetOfBugs)
        {
            if(bug.getLineNumber()!=line||!bug.getFileName().equals(fileOfElement))
            {
                continue;
            }

            String bugName=bug.getName();
            String[] split = bugName.split("\"");
            for(String bugword:split)
            {
                if(targetName.equals(bugword))
                {
                    try
                    {
                        thisBug = new Bug(bug);
                        thisBugName = bugword;
                        return true;
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void process(CtField element) {
        element.addModifier(ModifierKind.TRANSIENT);
        CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        final String value = String.format("/*[Spoon inserted modifier], repairs sonarqube rule 1948:\nFields in a \"Serializable\" class should either be transient or serializable*/");
        snippet.setValue(value);
        CtComment comment = getFactory().createComment(value,CtComment.CommentType.BLOCK);
//        element.addComment(comment);
    }
}