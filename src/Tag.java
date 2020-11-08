import java.util.List;

public class Tag {

    private String tag = "";
    private String content;

    private boolean valid;

    public Tag(String value){

        valid = false;

        //< ... >
        if(value.startsWith("<")&&value.endsWith(">")){
            String[] split = value.split(">");
            String fullStartTag = split[0] + ">";
            String[] startTagArgs=fullStartTag.substring(1, fullStartTag.length()-1).split(" ");
            for(int index = 0; index<startTagArgs.length&&!(startTagArgs[index].contains("=")||(index+1<startTagArgs.length&&startTagArgs[index+1].startsWith("="))); index++){
                tag+=startTagArgs[index]+" ";
            }
            tag = tag.trim();

            //<tag> ... </tag>
            if(value.endsWith("</"+tag+">")){
                valid = true;
                content = value.substring(fullStartTag.length(),value.length()-(tag.length()+3));
            }

            //<tag>
            if(value.equals(fullStartTag)){
                valid = true;
            }
        }else{
            valid = true;
            content = " "+value.trim();
        }

    }

    public String getContent(){
        String result = "";
        if(!tag.equals("")){
            result = "\n";
        }
        if(valid){
            if(content==null||content.equals("")){
                result += tag+": no content";
            }else {
                result += content;
            }
        }else{
            result += tag+": error";
        }
        return result;
    }

}
