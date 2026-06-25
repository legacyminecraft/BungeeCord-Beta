package com.legacyminecraft.bungee;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;
import java.util.jar.Manifest;

public record BungeeBuildInformation(
        String implVersion,
        Optional<String> gitBranch,
        Optional<String> gitCommit,
        Optional<Instant> buildTimestamp
) {
    private static final String ATTRIBUTE_IMPL_VERSION = "Implementation-Version";
    public static final String ATTRIBUTE_GIT_BRANCH = "Git-Branch";
    public static final String ATTRIBUTE_GIT_COMMIT = "Git-Commit";
    public static final String ATTRIBUTE_BUILD_TIMESTAMP = "Build-Timestamp";

    public BungeeBuildInformation() {
        Manifest manifest;
        try (InputStream input = BungeeBuildInformation.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF")) {
            manifest = new Manifest(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load build information", e);
        }

        this(
                getManifestAttribute(manifest, ATTRIBUTE_IMPL_VERSION).orElseThrow(),
                getManifestAttribute(manifest, ATTRIBUTE_GIT_BRANCH),
                getManifestAttribute(manifest, ATTRIBUTE_GIT_COMMIT),
                getManifestAttribute(manifest, ATTRIBUTE_BUILD_TIMESTAMP).map(Instant::parse)
        );
    }

    public String asFullVersionString() {
        StringBuilder sb = new StringBuilder();
        sb.append(implVersion());
        gitBranch().ifPresentOrElse(gitBranch -> {
            sb.append("-").append(gitBranch);
            gitCommit().ifPresent(gitCommit -> sb.append("@").append(gitCommit));
        }, () -> sb.append("-").append("DEV"));
        buildTimestamp().ifPresent(buildTimestamp -> sb.append(" (").append(buildTimestamp).append(")"));
        return sb.toString();
    }

    private static Optional<String> getManifestAttribute(Manifest manifest, String attribute) {
        String value = manifest.getMainAttributes().getValue(attribute);
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(value);
    }
}
