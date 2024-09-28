package com.epam.esm.gym.workload.runner;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Abstract base class for all test classes.
 * This class integrates a custom JUnit extension for shared test behaviors.
 */
@Slf4j
class RunnerExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        boolean testResult = context.getExecutionException().isPresent();
        log.info(Boolean.toString(testResult));
        log.info(context.getDisplayName());
        TestBase.isTestSuccessful = !testResult;
    }
}
