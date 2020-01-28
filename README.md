# top-js-libs

This application prints names of top JS libs among all the web-pages from the top of Google results for the given query.

## Usage

Use `./gradlew run` to run the application and then enter the query.

You may also create a distribution using `./gradlew distZip`.

## Implementation details

### 1. Google results retrieval

To retrieve the results page for the query I send HTTP query to `google.com/search` and pass parameters `q` for query and `start` for index of the first result (as Google has 10 results per page).

I also mimic browser using `User-Agent` header (which is required to receive correct results, but in real world it's better to use APIs).

### 2. Extract main result links from the page

Google uses particular tag for the search results: 

```<div class="r"><a href="Link to the result"...>...</a></div>```

I use regular expressions to extract it.

But if I was not limited by JDK only, I'd prefer to use some HTML parsing library like `JSoup`.

### 3. Page download and libs extraction

To download pages I use `java.net.HttpURLConnection` class.

I supposed that JS libs are the libs contained in the `<script>` `src` attribute. 
And I extract them using regex and leave only the filename.

This is not fully correct. 
Whether there are a lot of ways to do it better, e.g. parsing JS on the page and liked sources; 
I consider the chosen approach a reasonable simplification, because of time limitations and ambiguity of JS lib definition.

### 4. Top results

After all the names have been extracted, I build a Map of names to frequency and then extract top names using the priority queue.

## Bonus Steps

### Testing

I used `JUnit` for unit tests. 
Because of the networking, some methods behavior is purely predictable. 
If I had more then 3 hours, I'd add more environment-independent tests using `Mockito`. 

### Concurrency

My approach was designed in a form of a pipeline. 
I used Java Stream API to integrate all the components as a pipeline.
So it was quite easy to use a `ParallelStream`, which demonstrated a good speed-up compared to a single-threaded Stream.

However, I think performance may be increased by exploiting the benefits of asynchronous IO, as we have to download a lot of independent data via network.
Without JDK limitations, I'd probably try to use some async framework.

P.S. `NIO` is capable of async IO and it's in JDK, but it requires more time to design a proper solution using it. 

### Deduplication 

I think of 2 approaches:

- Using some list of known libraries and determining which is which using substring or some more intelligent comparison method.
- Set of transformation rules, e.g. lowercase, cutting version suffixes with regex.
