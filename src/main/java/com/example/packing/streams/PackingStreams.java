package com.example.packing.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface PackingStreams {
    public String PICK_OUTPUT="pick-out";
    public String PACK_OUTPUT="pack-out";
    
    @Input(PICK_OUTPUT)
    public SubscribableChannel outboundPick();

    @Output(PACK_OUTPUT)
    public MessageChannel outboundPack();

}