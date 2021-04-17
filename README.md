# Youtube Data Scraper Java Library

This library is Java 11 Youtube public API HTTP client. Basically this tool is designed to retrieve
and store in a database all publicly available comments for any youtube channel by a provided channel Id.

Links to related repositories:  
[Youtube Scraper SpringBoot Web App](https://github.com/alexshavlovsky/yts-service.git).  
[Youtube Scraper Web App Angular Client](https://github.com/alexshavlovsky/yts-client.git).

Main features:
- supported endpoints:
    - youtube.com/channel/%s
    - youtube.com/%s/videos
    - youtube.com/watch
    - youtube.com/browse_ajax      
    - youtube.com/comment_service_ajax
    - youtube.com/youtubei/v1/browse
- supported entities:
    - channel metadata 
    - channel microformat
    - channel video
    - video comment / reply
- fetching data:
    - channel metadata by channel ID
    - videos list by channel ID
    - comments and replies by video ID
    - comments and replies by channel ID
- supported comment fetching modes:
    - top comments first
    - newest comments first
- store data:
    - H2 database / Hibernate
    - filesystem

## Usage examples

Get channel metadata:
``` JAVA
    String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
    YoutubeChannelMetadataClient channelHttpClient = new YoutubeChannelMetadataClient(channelId);
    System.out.println(channelHttpClient.getChannelMetadata());
    System.out.println(channelHttpClient.getChannelMicroformat());
    System.out.println(channelHttpClient.getChannelHeader());
    System.out.println(channelHttpClient.getChannelMetadata().getVanityChannelUrl());
    System.out.println(channelHttpClient.getChannelHeader().getSubscriberCountText());
    System.out.println(channelHttpClient.getChannelVanityName());
```

Get video list by channel ID:
``` JAVA
    String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
    ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
    ChannelVideosDTO channel = collector.call();
    List<VideoDTO> videos = channel.getVideos();
    for (int i = 0; i < videos.size(); i++) {
        VideoDTO video = videos.get(i);
        System.out.println(String.format("%s [%s] %s", i + 1, video.getVideoId(), video.getTitle()));
    }
```

Print comments to console by a video ID:
``` JAVA
    String videoId = "ipAnwilMncI";
    Runnable runner = CommentRunnerFactory.newInstance(
            videoId,
            new CommentConsolePrinter(new CommentHumanReadableFormatter()),
            CommentOrderCfg.TOP_FIRST,
            CommentIteratorCfg.newInstance(1000, 10)
    );
    runner.run();
```

Get comments by list of video IDs:
``` JAVA
    String[] ids = {
            "D2bB1bz9Z9s", "LqihfRVj8hM", "_oaSgmoy9aA", "lIlSNpLkO-A", "XQ_cQ9I7_YA",
            "Dtk2xgBZTec", "pEr1TtCB7_Y", "NMg6DQSO5VE", "bhE2RaN4VcI", "pJJE7R8xteQ"
    };
    CustomExecutorService executor = CustomExecutorService.newInstance();
    Arrays.stream(ids).map(videoId -> newDefaultFileAppender(videoId, CommentOrderCfg.NEWEST_FIRST)).forEach(executor::submit);
    executor.awaitAndTerminate();
```

Get all channel comments by channel ID:
``` JAVA
    String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
    ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
    ChannelVideosDTO channelVideos = collector.call();
    CustomExecutorService executor = CustomExecutorService.configure()
            .numberOfThreads(10).timeout(Duration.ofMinutes(10)).toBuilder().build();
    channelVideos.getVideos().stream().map(
            v -> newDefaultFileAppender(v.getVideoId(), CommentOrderCfg.NEWEST_FIRST)
    ).forEach(executor::submit);
    executor.awaitAndTerminate();
```

Store channel comments to a database:
``` JAVA
    String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
    HibernateChannelRunner.newBuilder(channelId)
            .withExecutor(20, Duration.ofHours(1))
            .processAllChannelComments().build().call();
```

## Technology Stack

Component          | Technology
---                | ---
Runtime            | Java 11
Http client        | java.net.http.HttpClient, [Brotli decoder](https://github.com/google/brotli)
Data mapping       | Jackson, [ModelMapper](https://github.com/modelmapper/modelmapper)
Data persistence   | Hibernate 5, H2 database, PostgreSQL
