package tree.directory.workers;

import tree.directory.models.LineInfo;
import tree.directory.structures.BlockingStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Damyan Manev on 17-Jan-16.
 */
public class LineConsumer extends Thread {

    private MatchFoundListener matchFoundListener;
    private boolean testing = false;
    private Set<LineInfo> set = Collections.synchronizedSet(new HashSet<>());

    private BlockingStorage<LineInfo> blockingStorage;
    private String targetWord;

    public LineConsumer(String name, String targetWord, BlockingStorage<LineInfo> blockingStorage, MatchFoundListener matchFoundListener) {
        super(name);
        this.targetWord = targetWord;
        this.blockingStorage = blockingStorage;
        this.matchFoundListener = matchFoundListener;
    }

    @Override
    public void run() {
        LineInfo lineInfo;
        try {
            while ((lineInfo = blockingStorage.get()) != null) {
                if (lineInfo.getLine().contains(targetWord)) {
                    if (testing) {
                        set.add(lineInfo);
                    } else {
                        matchFoundListener.onMatchFound(lineInfo);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface MatchFoundListener {
        void onMatchFound(LineInfo lineInfo);
    }
}
