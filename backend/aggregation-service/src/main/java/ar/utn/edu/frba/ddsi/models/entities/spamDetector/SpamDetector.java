package ar.utn.edu.frba.ddsi.models.entities.spamDetector;

import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SpamDetector implements ISpamDetector {

  private final Set<String> SPANISH_STOPWORDS = Set.of(
      "a", "al", "algo", "algunas", "algunos", "ante", "antes", "como", "con", "contra",
      "cual", "cuando", "de", "del", "desde", "donde", "durante", "e", "el", "ella",
      "ellas", "ellos", "en", "entre", "esa", "esas", "ese", "eso", "esos", "esta",
      "estas", "este", "esto", "estos", "estar", "estoy", "fin", "hasta", "hay",
      "la", "las", "le", "les", "lo", "los", "mas", "más", "me", "mia", "mias", "mio",
      "mios", "mi", "mía", "mías", "mío", "míos", "mis", "mucho", "muchos", "muy",
      "nada", "ni", "no", "nos", "nosotras", "nosotros", "nuestra", "nuestras",
      "nuestro", "nuestros", "o", "otra", "otras", "otro", "otros", "para", "pero",
      "poco", "por", "porque", "qué", "que", "quien", "quienes", "se", "sin", "sobre",
      "su", "sus", "suya", "suyas", "suyo", "suyos", "sí", "también", "tanto", "te",
      "ti", "todo", "todos", "tu", "tus", "tuyas", "tuyo", "tuyos", "tú", "un", "una",
      "uno", "unos", "yo", "ya"
  );

  @Override
  public boolean isSpam(List<DeletionRequest> eventDeletionRequests, String argument) {
    String[] processedArgument = preprocess(argument);

    // Get all the arguments from DeletionRequests of the same event.
    List<String[]> processedEventArguments = eventDeletionRequests
        .stream()
        .map(dr -> preprocess(dr.getArgument()))
        .collect(Collectors.toList());

    // 1. Check frequency within the argument itself
    boolean argumentHasSpam = detectSpamByArgumentFrequency(processedArgument);
    if (argumentHasSpam) return true;

    // 2. Check similarity against history
    boolean argumentsHaveSpam = detectSpamByHistoryArguments(processedEventArguments, processedArgument);
    return argumentsHaveSpam;
  }

  private String[] preprocess(String argument) {
    return Arrays.stream(argument.toLowerCase()
            .replaceAll("[^a-zA-Záéíóúüñ0-9\\s]", "") // Remove punctuation but allow accents
            .split("\\s+")) // Split by whitespace
        .filter(word -> !SPANISH_STOPWORDS.contains(word)) // Remove stopwords
        .toArray(String[]::new);
  }

  private boolean detectSpamByArgumentFrequency(String[] argumentWords) {
    if (argumentWords.length == 0) return false;

    Map<String, Integer> wordCounts = new HashMap<>();
    double totalWords = argumentWords.length;

    for (String word : argumentWords) {
      wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
    }

    // If only uses 10 words or less (unique words), checks simply by count.
    double uniqueWordsCount = wordCounts.size();

    if (totalWords > 20 && uniqueWordsCount <= 7) return true;

    double threshold = 0.25; // 25% of frequency allowed

    // Checks if a word has a high frequency.
    for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
      double frequency = entry.getValue() / totalWords;

      if (frequency > threshold) {
        return true;
      }
    }

    return false;
  }

  private boolean detectSpamByHistoryArguments(List<String[]> historyArguments, String[] currentArgument) {
    if (historyArguments.isEmpty() || currentArgument.length == 0) return false;

    Set<String> currentBigrams = generateBigramsFromArray(currentArgument);

    if (currentBigrams.isEmpty()) return false;

    double threshold = 0.7; // 70% of similarity allowed


    for (String[] historyArg : historyArguments) {
      if (historyArg.length == 0) continue;

      Set<String> historyBigrams = generateBigramsFromArray(historyArg);

      if (calculateJaccardSimilarity(currentBigrams, historyBigrams) > threshold)
        return true;
    }
    return false;
  }

  private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
    if (set1.isEmpty() || set2.isEmpty()) return 0.0;

    Set<String> intersection = new HashSet<>(set1);
    intersection.retainAll(set2);

    double intersectionSize = intersection.size();
    // Union = A + B - Intersection
    double unionSize = set1.size() + set2.size() - intersectionSize;

    if (unionSize == 0) return 0.0;

    return intersectionSize / unionSize;
  }

  private Set<String> generateBigramsFromArray(String[] words) {
    Set<String> bigrams = new HashSet<>();

    if (words.length < 2) return bigrams;

    // Create bigrams ("hello world", "world class")
    for (int i = 0; i < words.length - 1; i++) {
      bigrams.add(words[i] + " " + words[i + 1]);
    }
    return bigrams;
  }

}