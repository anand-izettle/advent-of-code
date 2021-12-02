package se.zettle.adventofcode.provider;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component
@Getter
public class InputProvider {

    private final static String BASE_FOLDER = "puzzle/inputs";
    private final static Map<String, String> inputMap = new HashMap<>();

    private final ResourceLoader resourceLoader;

    public InputProvider(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        inputMap.putAll(readAllInputs());
    }

    public static String getPuzzleInput(String fileName) {
        String keyName = inputMap.keySet().stream()
            .filter(k -> k.equalsIgnoreCase(fileName))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find file: %s", fileName)));

        return inputMap.get(keyName);
    }

    public static Map<String, String> getInputMap() {
        return inputMap;
    }

    private Map<String, String> readAllInputs() {

        List<Resource> resources = getResources(BASE_FOLDER);
        return resources.stream()
            .map(this::getFileNameAndContents)
            .filter(Predicate.not(e -> e.getValue().isBlank()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private SimpleImmutableEntry<String, String> getFileNameAndContents(final Resource resource) {

        String fileName = Objects.requireNonNull(getFileName(resource), "Could not get FileName");
        String fileContents = getFileContents(resource);
        return new SimpleImmutableEntry<>(fileName, fileContents);
    }

    private String getFileContents(final Resource resource) {

        try {
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Resource> getResources(final String basePath) {

        try {
            return Arrays.stream(ResourcePatternUtils
                    .getResourcePatternResolver(resourceLoader)
                    .getResources(String.format("classpath*:/%s/**", basePath)))
                .filter(Resource::isFile)
                .filter(Resource::isReadable)
                .toList();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("No inputs");
        }

    }

    private String getFileName(final Resource f) {

        try {
            String path = f.getFile().getPath();

            int resources = path.indexOf(BASE_FOLDER);

            return path.substring(resources + BASE_FOLDER.length() + 1)
                .replace("/", "")
                .replace(".txt", "");

        } catch (IOException e) {
            return f.getFilename();
        }

    }

}
