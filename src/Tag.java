public class Tag {

    private String value;
    private String fullStartTag;

    private String tag;
    private String content;

    public Tag(String value){

        //< ... >
        if(value.startsWith("<")&&value.endsWith(">")){
            String[] split = value.split(">");
            fullStartTag = split[0]+">";
            tag = fullStartTag.substring(1,fullStartTag.length()-1).split(" ")[0];

            //< ... > ... </ ... >
            if(!value.equals(fullStartTag)){
                content = value.substring(fullStartTag.length(),value.length()-(tag.length()+2));
            }
        }

        //< ... > ...
        //< ...
        if(value.startsWith("<")&&!value.endsWith(">")){
            tag = "";
        }

        //< ... > ... > is handled in "isValid()"

        this.value = value;
    }

    public boolean isValid(){
        //Lorem ipsum (no tags)
        if(tag == null){
            return true;
        }

        //<p>Lorem ipsum</p>
        //<img ...>
        if(value.endsWith("</"+tag+">")||value.equals(fullStartTag)){
            return true;
        }else{
            return false;
        }
    }

    public String getContent(){
        if(isValid()){
            if(content==null){
                return tag+": no content";
            }
            return content;
        }else{
            return tag+": error";
        }
    }

}
