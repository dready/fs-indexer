# Simple FileSystem Indexer

A simple FileSystem Indexer capable of walking through a specific Directory recursively and if configured registering
all Directories on it's way to a watchdog. The watchdog then will call the indexer if a directory has been modified or
created.

## Requirements
Java 7

## Usages

To use the Indexer you can use it directly and providing it with your FileVisitor<Path> or you can use the
IndexingThread for it.

```java
Indexer indexer = new Indexer();
indexer.setVisitor(myFileVisitor);
indexer.index(myDirectory);

// if using the Thread
IndexingThread thread = new IndexingThread();
thread.setIndexer(indexer);
thread.run();
```