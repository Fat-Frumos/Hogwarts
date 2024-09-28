package com.epam.esm.gym.jms;

import org.springframework.context.annotation.Configuration;

/**
 * ActiveMQConfig configures queues for profile requests, responses, and dead letter handling.
 */
@Configuration
public class ActiveMQConfig {
    public static final String TRAINER_SUMMARY_QUEUE = "trainer.summary.queue";
    public static final String DELETE_TRAINING_QUEUE = "delete.training.queue";
    public static final String ADD_TRAINING_QUEUE = "add.training.queue";
    public static final String TRAINER_QUEUE = "trainer.profile.queue";
    public static final String TRAINERS_QUEUE = "trainers.list.queue";
    public static final String USERNAME_QUEUE = "trainer.name.queue";
    public static final String ERROR_QUEUE = "trainer.profile.dlq";
}