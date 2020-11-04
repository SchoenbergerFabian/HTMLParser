import java.util.List;

public class Tag {

    private String tag;
    private String content;

    private boolean valid;

    public Tag(String value){

        valid = false;

        //< ... >
        if(value.startsWith("<")&&value.endsWith(">")){
            String[] split = value.split(">");
            String fullStartTag = split[0] + ">";
            tag = fullStartTag.substring(1, fullStartTag.length()-1).split(" ")[0];

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
            content = value;
        }

    }

    public String getContent(){
        String result = "\n";
        if(valid){
            if(content==null||content.equals("")){
                result += tag+": no content";
            }
            result += content;
        }else{
            result += tag+": error";
        }
        return result;
    }

}
