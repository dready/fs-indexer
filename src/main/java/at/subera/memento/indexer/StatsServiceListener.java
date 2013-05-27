package at.subera.memento.indexer;

import at.subera.fs.indexer.listener.IndexListenable;
import at.subera.memento.rest.service.StatsService;

import java.nio.file.Path;

public class StatsServiceListener implements IndexListenable<Path>
{
    private StatsService statsService;

    public void ßsetStatsService(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public void preIndex(String directory) {
        //todo implement
//        statsService.startIndexing(directory);
    }

    @Override
    public void postIndex(String directory) {
        //todo implement
//        statsService.endIndexing(directory);
    }
}
