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

    // Get all the arguments from dr's of the same event.
    List<String[]> processedEventArguments = eventDeletionRequests.stream()
        .map(dr -> preprocess(dr.getArgument()))
        .collect(Collectors.toList());

    processedEventArguments.add(processedArgument); // include given argument to calculate idf.

    Map<String, Double> tf = computeTF(processedArgument); // Counts terms frequency in given argument.
    Map<String, Double> idf = computeIDF(processedEventArguments);
    Map<String, Double> tfidf = computeTFIDF(tf, idf);

    // Criterio simple: si alguna palabra de SPAM tiene TF-IDF alto
    return tfidf.entrySet().stream()
        .anyMatch(entry -> entry.getValue() > 0.2); // TODO: 0.2 is a test value, must be adjusted
  }

  private String[] preprocess(String argument) {
    return Arrays.stream(argument.toLowerCase()
            .replaceAll("[^a-zA-Záéíóúüñ0-9\\s]", "") // remove punctuation but allow accents
            .split("\\s+")) // split by whitespace
        .filter(word -> !SPANISH_STOPWORDS.contains(word)) // remove stopwords
        .toArray(String[]::new);
  }

  // Counts frequency of terms in argument
  private Map<String, Double> computeTF(String[] argumentArray) {
    Map<String, Double> tf = new HashMap<>();
    double total = argumentArray.length;

    // Counts the occurrences of each word
    for (String word : argumentArray) {
      tf.put(word, tf.getOrDefault(word, 0.0) + 1.0);
    }

    // Normalize the values by dividing each count by the total number of words
    for (String word : tf.keySet()) {
      tf.put(word, tf.get(word) / total);
    }

    return tf; // Return the map of term frequencies
  }

  private Map<String, Double> computeIDF(List<String[]> allDocs) {
    Map<String, Double> idf = new HashMap<>();
    int totalDocs = allDocs.size();

    // Counts the occurrences of each word for each argument.
    for (String[] doc : allDocs) {
      Set<String> unique = new HashSet<>(Arrays.asList(doc));
      for (String word : unique) {
        idf.put(word, idf.getOrDefault(word, 0.0) + 1.0);
      }
    }

    for (String word : idf.keySet()) {
      idf.put(word, Math.log((double) totalDocs / idf.get(word)));
    }

    return idf;
  }

  private Map<String, Double> computeTFIDF(Map<String, Double> tf, Map<String, Double> idf) {
    Map<String, Double> tfidf = new HashMap<>();

    // Calculates  tf-idf for each word.
    for (String word : tf.keySet()) {
      tfidf.put(word, tf.get(word) * idf.getOrDefault(word, 0.0));
    }

    return tfidf;
  }
}
