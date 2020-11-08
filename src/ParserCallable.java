import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParserCallable implements Callable<PrioritisedString> {

    private int priority;
    private Tag tag;

    public ParserCallable(int priority, Tag tag) {
        this.priority = priority;
        this.tag = tag;
    }

    @Override
    public PrioritisedString call() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();

        List<PrioritisedString> splits = getSplit(tag.getContent());

        List<ParserCallable> parsers = new ArrayList<>();
        for(int i = 0; i<splits.size(); i++){
            if(splits.get(i).getString().startsWith("<")){
                parsers.add(new ParserCallable(splits.get(i).getPriority(),new Tag(splits.get(i).getString())));
            }
        }

        List<Future<PrioritisedString>> results = executor.invokeAll(parsers);
        results.stream()
        .map(result -> {
            try {
                return result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(result -> {
            splits.set(splits.indexOf(result), result);
        });

        executor.shutdown();

        StringBuilder result = new StringBuilder();
        splits.forEach(prioritisedString -> result.append(prioritisedString.getString()));
        return new PrioritisedString(priority, result.toString());
    }

    public static List<PrioritisedString> getSplit(String content){
        List<PrioritisedString> splits = new ArrayList<>();

        StringBuilder split = new StringBuilder();
        int counter = 1;
        for(int index = 0; index<content.length();index++){

            if(index+1<content.length() && content.charAt(index) == '<' && content.charAt(index+1) != '/'){
                //adding previous saved string
                if(!split.toString().equals("")){
                    splits.add(new PrioritisedString(counter,split.toString()));
                    counter++;
                }
                split = new StringBuilder();

                //getting the startingTag
                int tagIndex;
                for(tagIndex = index; content.charAt(tagIndex)!='>'; tagIndex++){
                    split.append(content.charAt(tagIndex));
                }
                split.append(">");
                index = tagIndex;

                int count = 1;
                StringBuilder possibleContent = new StringBuilder();
                for(int innerIndex = tagIndex+1; innerIndex<content.length(); innerIndex++){
                    if(content.charAt(innerIndex)=='<' && content.charAt(innerIndex+1)!='/'){

                        possibleContent.append(content.charAt(innerIndex));
                        count++;

                    }else if(content.charAt(innerIndex)=='<' && content.charAt(innerIndex+1)=='/'){

                        count--;
                        if(count==0){
                            split.append(possibleContent.toString());
                            for(innerIndex = innerIndex; content.charAt(innerIndex)!='>'; innerIndex++){
                                split.append(content.charAt(innerIndex));
                            }
                            split.append(">");
                            index = innerIndex;

                            splits.add(new PrioritisedString(counter,split.toString()));
                            counter++;
                            split = new StringBuilder();
                            break;
                        }else{
                            possibleContent.append(content.charAt(innerIndex));
                        }

                    }else{

                        possibleContent.append(content.charAt(innerIndex));

                    }
                }
                //add if not ""
                //= add if split is a single tag
                if(!split.toString().equals("")){
                    splits.add(new PrioritisedString(counter,split.toString()));
                    counter++;
                    split = new StringBuilder();
                }

            }else{
                split.append(content.charAt(index));
            }

        }
        //add if not already added
        //= add if last split is not a tag
        if(!split.toString().trim().equals("")&&!splits.contains(split.toString())){
            splits.add(new PrioritisedString(counter,split.toString()));
            //counter++ not needed
        }

        return splits;
    }

}
