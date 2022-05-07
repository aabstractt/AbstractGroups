package dev.thatsmybaby.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public final class VersionInfo {

    @Getter private static Logger logger;

    public static void loadVersion() {
        logger = LogManager.getLogger("AbstractGroups");
    }
}