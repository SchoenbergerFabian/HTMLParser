import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParserCallable implements Callable<String> {

    private Tag tag;

    public ParserCallable(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String call() throws Exception {
        Executor threadpool = Executors.newCachedThreadPool();

        return null;
    }

    public List<Tag> getInsideTags(){

    }
}
