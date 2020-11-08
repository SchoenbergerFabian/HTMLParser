import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static final File file = new File("simple1.txt");

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        List<PrioritisedString> tags = new ArrayList<>();
        try {
            tags = ParserCallable.getSplit(readFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<ParserCallable> parser = new ArrayList<>();
        for (PrioritisedString string: tags) {
            if(!string.getString().trim().equals("")){
                parser.add(new ParserCallable(string.getPriority(), new Tag(string.getString())));
            }
        }

        List<Future<PrioritisedString>> resultFutures = new ArrayList<>();
        try {
            resultFutures = executor.invokeAll(parser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resultFutures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).sorted()
                .map(prioritisedString -> prioritisedString.getString())
                .forEach(System.out::print);

        executor.shutdown();
    }

    private static String readFile(File file) throws IOException {
        return Files.lines(file.toPath())
                .reduce((string1,string2)->string1+string2)
                .orElse("");
    }
}
