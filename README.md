# Youtube Data Scraper Java Library

This library is Java 11 Youtube public API HTTP client. Main features:
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

Get videos list by channel ID:
``` JAVA
    String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
    ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
    ChannelDTO channel = collector.call();
    List<VideoDTO> videos = channel.getVideos();
    for (int i = 0; i < videos.size(); i++) {
        VideoDTO video = videos.get(i);
        System.out.println(String.format("%s [%s] %s", i + 1, video.getVideoId(), video.getTitle()));
    }
```

Get comments by list of video IDs:
``` JAVA
    String[] ids = {
            "D2bB1bz9Z9s", "LqihfRVj8hM", "_oaSgmoy9aA", "lIlSNpLkO-A", "XQ_cQ9I7_YA",
            "Dtk2xgBZTec", "pEr1TtCB7_Y", "NMg6DQSO5VE", "bhE2RaN4VcI", "pJJE7R8xteQ"
    };
    CustomExecutorService executor = new CustomExecutorService("CommentWorker", 10, 5, TimeUnit.MINUTES);
    Arrays.stream(ids).map(videoId -> newDefaultFileAppender(videoId, true)).forEach(executor::submit);
    executor.awaitAndTerminate();
```

## Technology Stack

Component          | Technology
---                | ---
Runtime            | Java 11
Http client        | java.net.http.HttpClient, [Brotli decoder](https://github.com/google/brotli)
Data mapping       | Jackson, [ModelMapper](https://github.com/modelmapper/modelmapper)
Data persistence   | Hibernate 5, H2 database
