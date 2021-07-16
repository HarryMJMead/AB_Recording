// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: simulator.proto

package ab.proto;

public final class Simulator {
  private Simulator() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface StateOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ab.proto.State)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 pigs = 1;</code>
     */
    int getPigs();

    /**
     * <code>int32 birds = 2;</code>
     */
    int getBirds();

    /**
     * <code>int32 end = 3;</code>
     */
    int getEnd();
  }
  /**
   * Protobuf type {@code ab.proto.State}
   */
  public  static final class State extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ab.proto.State)
      StateOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use State.newBuilder() to construct.
    private State(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private State() {
      pigs_ = 0;
      birds_ = 0;
      end_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private State(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              pigs_ = input.readInt32();
              break;
            }
            case 16: {

              birds_ = input.readInt32();
              break;
            }
            case 24: {

              end_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ab.proto.Simulator.internal_static_ab_proto_State_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ab.proto.Simulator.internal_static_ab_proto_State_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ab.proto.Simulator.State.class, ab.proto.Simulator.State.Builder.class);
    }

    public static final int PIGS_FIELD_NUMBER = 1;
    private int pigs_;
    /**
     * <code>int32 pigs = 1;</code>
     */
    public int getPigs() {
      return pigs_;
    }

    public static final int BIRDS_FIELD_NUMBER = 2;
    private int birds_;
    /**
     * <code>int32 birds = 2;</code>
     */
    public int getBirds() {
      return birds_;
    }

    public static final int END_FIELD_NUMBER = 3;
    private int end_;
    /**
     * <code>int32 end = 3;</code>
     */
    public int getEnd() {
      return end_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (pigs_ != 0) {
        output.writeInt32(1, pigs_);
      }
      if (birds_ != 0) {
        output.writeInt32(2, birds_);
      }
      if (end_ != 0) {
        output.writeInt32(3, end_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (pigs_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, pigs_);
      }
      if (birds_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, birds_);
      }
      if (end_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, end_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ab.proto.Simulator.State)) {
        return super.equals(obj);
      }
      ab.proto.Simulator.State other = (ab.proto.Simulator.State) obj;

      boolean result = true;
      result = result && (getPigs()
          == other.getPigs());
      result = result && (getBirds()
          == other.getBirds());
      result = result && (getEnd()
          == other.getEnd());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + PIGS_FIELD_NUMBER;
      hash = (53 * hash) + getPigs();
      hash = (37 * hash) + BIRDS_FIELD_NUMBER;
      hash = (53 * hash) + getBirds();
      hash = (37 * hash) + END_FIELD_NUMBER;
      hash = (53 * hash) + getEnd();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ab.proto.Simulator.State parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ab.proto.Simulator.State parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ab.proto.Simulator.State parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ab.proto.Simulator.State parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ab.proto.Simulator.State parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ab.proto.Simulator.State parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ab.proto.Simulator.State parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ab.proto.Simulator.State parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ab.proto.Simulator.State parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ab.proto.Simulator.State parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ab.proto.Simulator.State parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ab.proto.Simulator.State parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ab.proto.Simulator.State prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ab.proto.State}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ab.proto.State)
        ab.proto.Simulator.StateOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ab.proto.Simulator.internal_static_ab_proto_State_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ab.proto.Simulator.internal_static_ab_proto_State_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ab.proto.Simulator.State.class, ab.proto.Simulator.State.Builder.class);
      }

      // Construct using ab.proto.Simulator.State.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        pigs_ = 0;

        birds_ = 0;

        end_ = 0;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ab.proto.Simulator.internal_static_ab_proto_State_descriptor;
      }

      public ab.proto.Simulator.State getDefaultInstanceForType() {
        return ab.proto.Simulator.State.getDefaultInstance();
      }

      public ab.proto.Simulator.State build() {
        ab.proto.Simulator.State result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ab.proto.Simulator.State buildPartial() {
        ab.proto.Simulator.State result = new ab.proto.Simulator.State(this);
        result.pigs_ = pigs_;
        result.birds_ = birds_;
        result.end_ = end_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ab.proto.Simulator.State) {
          return mergeFrom((ab.proto.Simulator.State)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ab.proto.Simulator.State other) {
        if (other == ab.proto.Simulator.State.getDefaultInstance()) return this;
        if (other.getPigs() != 0) {
          setPigs(other.getPigs());
        }
        if (other.getBirds() != 0) {
          setBirds(other.getBirds());
        }
        if (other.getEnd() != 0) {
          setEnd(other.getEnd());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ab.proto.Simulator.State parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ab.proto.Simulator.State) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int pigs_ ;
      /**
       * <code>int32 pigs = 1;</code>
       */
      public int getPigs() {
        return pigs_;
      }
      /**
       * <code>int32 pigs = 1;</code>
       */
      public Builder setPigs(int value) {
        
        pigs_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 pigs = 1;</code>
       */
      public Builder clearPigs() {
        
        pigs_ = 0;
        onChanged();
        return this;
      }

      private int birds_ ;
      /**
       * <code>int32 birds = 2;</code>
       */
      public int getBirds() {
        return birds_;
      }
      /**
       * <code>int32 birds = 2;</code>
       */
      public Builder setBirds(int value) {
        
        birds_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 birds = 2;</code>
       */
      public Builder clearBirds() {
        
        birds_ = 0;
        onChanged();
        return this;
      }

      private int end_ ;
      /**
       * <code>int32 end = 3;</code>
       */
      public int getEnd() {
        return end_;
      }
      /**
       * <code>int32 end = 3;</code>
       */
      public Builder setEnd(int value) {
        
        end_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 end = 3;</code>
       */
      public Builder clearEnd() {
        
        end_ = 0;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:ab.proto.State)
    }

    // @@protoc_insertion_point(class_scope:ab.proto.State)
    private static final ab.proto.Simulator.State DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ab.proto.Simulator.State();
    }

    public static ab.proto.Simulator.State getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<State>
        PARSER = new com.google.protobuf.AbstractParser<State>() {
      public State parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new State(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<State> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<State> getParserForType() {
      return PARSER;
    }

    public ab.proto.Simulator.State getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public interface ActionOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ab.proto.Action)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 agent = 1;</code>
     */
    int getAgent();
  }
  /**
   * Protobuf type {@code ab.proto.Action}
   */
  public  static final class Action extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ab.proto.Action)
      ActionOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Action.newBuilder() to construct.
    private Action(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Action() {
      agent_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Action(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              agent_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ab.proto.Simulator.internal_static_ab_proto_Action_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ab.proto.Simulator.internal_static_ab_proto_Action_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ab.proto.Simulator.Action.class, ab.proto.Simulator.Action.Builder.class);
    }

    public static final int AGENT_FIELD_NUMBER = 1;
    private int agent_;
    /**
     * <code>int32 agent = 1;</code>
     */
    public int getAgent() {
      return agent_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (agent_ != 0) {
        output.writeInt32(1, agent_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (agent_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, agent_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ab.proto.Simulator.Action)) {
        return super.equals(obj);
      }
      ab.proto.Simulator.Action other = (ab.proto.Simulator.Action) obj;

      boolean result = true;
      result = result && (getAgent()
          == other.getAgent());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + AGENT_FIELD_NUMBER;
      hash = (53 * hash) + getAgent();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ab.proto.Simulator.Action parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ab.proto.Simulator.Action parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ab.proto.Simulator.Action parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ab.proto.Simulator.Action parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ab.proto.Simulator.Action parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ab.proto.Simulator.Action parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ab.proto.Simulator.Action parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ab.proto.Simulator.Action parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ab.proto.Simulator.Action parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ab.proto.Simulator.Action parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ab.proto.Simulator.Action parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ab.proto.Simulator.Action parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ab.proto.Simulator.Action prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ab.proto.Action}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ab.proto.Action)
        ab.proto.Simulator.ActionOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ab.proto.Simulator.internal_static_ab_proto_Action_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ab.proto.Simulator.internal_static_ab_proto_Action_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ab.proto.Simulator.Action.class, ab.proto.Simulator.Action.Builder.class);
      }

      // Construct using ab.proto.Simulator.Action.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        agent_ = 0;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ab.proto.Simulator.internal_static_ab_proto_Action_descriptor;
      }

      public ab.proto.Simulator.Action getDefaultInstanceForType() {
        return ab.proto.Simulator.Action.getDefaultInstance();
      }

      public ab.proto.Simulator.Action build() {
        ab.proto.Simulator.Action result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ab.proto.Simulator.Action buildPartial() {
        ab.proto.Simulator.Action result = new ab.proto.Simulator.Action(this);
        result.agent_ = agent_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ab.proto.Simulator.Action) {
          return mergeFrom((ab.proto.Simulator.Action)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ab.proto.Simulator.Action other) {
        if (other == ab.proto.Simulator.Action.getDefaultInstance()) return this;
        if (other.getAgent() != 0) {
          setAgent(other.getAgent());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ab.proto.Simulator.Action parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ab.proto.Simulator.Action) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int agent_ ;
      /**
       * <code>int32 agent = 1;</code>
       */
      public int getAgent() {
        return agent_;
      }
      /**
       * <code>int32 agent = 1;</code>
       */
      public Builder setAgent(int value) {
        
        agent_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 agent = 1;</code>
       */
      public Builder clearAgent() {
        
        agent_ = 0;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:ab.proto.Action)
    }

    // @@protoc_insertion_point(class_scope:ab.proto.Action)
    private static final ab.proto.Simulator.Action DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ab.proto.Simulator.Action();
    }

    public static ab.proto.Simulator.Action getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Action>
        PARSER = new com.google.protobuf.AbstractParser<Action>() {
      public Action parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Action(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Action> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Action> getParserForType() {
      return PARSER;
    }

    public ab.proto.Simulator.Action getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ab_proto_State_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ab_proto_State_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ab_proto_Action_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ab_proto_Action_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017simulator.proto\022\010ab.proto\"1\n\005State\022\014\n\004" +
      "pigs\030\001 \001(\005\022\r\n\005birds\030\002 \001(\005\022\013\n\003end\030\003 \001(\005\"\027" +
      "\n\006Action\022\r\n\005agent\030\001 \001(\005b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_ab_proto_State_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ab_proto_State_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ab_proto_State_descriptor,
        new java.lang.String[] { "Pigs", "Birds", "End", });
    internal_static_ab_proto_Action_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_ab_proto_Action_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ab_proto_Action_descriptor,
        new java.lang.String[] { "Agent", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
