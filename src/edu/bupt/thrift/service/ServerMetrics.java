/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package edu.bupt.thrift.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMetrics implements org.apache.thrift.TBase<ServerMetrics, ServerMetrics._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ServerMetrics");

  private static final org.apache.thrift.protocol.TField SERVERIP_FIELD_DESC = new org.apache.thrift.protocol.TField("serverip", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField METRICS_FIELD_DESC = new org.apache.thrift.protocol.TField("metrics", org.apache.thrift.protocol.TType.DOUBLE, (short)2);

  public String serverip; // required
  public double metrics; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SERVERIP((short)1, "serverip"),
    METRICS((short)2, "metrics");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SERVERIP
          return SERVERIP;
        case 2: // METRICS
          return METRICS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __METRICS_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SERVERIP, new org.apache.thrift.meta_data.FieldMetaData("serverip", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.METRICS, new org.apache.thrift.meta_data.FieldMetaData("metrics", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ServerMetrics.class, metaDataMap);
  }

  public ServerMetrics() {
  }

  public ServerMetrics(
    String serverip,
    double metrics)
  {
    this();
    this.serverip = serverip;
    this.metrics = metrics;
    setMetricsIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ServerMetrics(ServerMetrics other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetServerip()) {
      this.serverip = other.serverip;
    }
    this.metrics = other.metrics;
  }

  public ServerMetrics deepCopy() {
    return new ServerMetrics(this);
  }

  @Override
  public void clear() {
    this.serverip = null;
    setMetricsIsSet(false);
    this.metrics = 0.0;
  }

  public String getServerip() {
    return this.serverip;
  }

  public ServerMetrics setServerip(String serverip) {
    this.serverip = serverip;
    return this;
  }

  public void unsetServerip() {
    this.serverip = null;
  }

  /** Returns true if field serverip is set (has been assigned a value) and false otherwise */
  public boolean isSetServerip() {
    return this.serverip != null;
  }

  public void setServeripIsSet(boolean value) {
    if (!value) {
      this.serverip = null;
    }
  }

  public double getMetrics() {
    return this.metrics;
  }

  public ServerMetrics setMetrics(double metrics) {
    this.metrics = metrics;
    setMetricsIsSet(true);
    return this;
  }

  public void unsetMetrics() {
    __isset_bit_vector.clear(__METRICS_ISSET_ID);
  }

  /** Returns true if field metrics is set (has been assigned a value) and false otherwise */
  public boolean isSetMetrics() {
    return __isset_bit_vector.get(__METRICS_ISSET_ID);
  }

  public void setMetricsIsSet(boolean value) {
    __isset_bit_vector.set(__METRICS_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SERVERIP:
      if (value == null) {
        unsetServerip();
      } else {
        setServerip((String)value);
      }
      break;

    case METRICS:
      if (value == null) {
        unsetMetrics();
      } else {
        setMetrics((Double)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SERVERIP:
      return getServerip();

    case METRICS:
      return Double.valueOf(getMetrics());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SERVERIP:
      return isSetServerip();
    case METRICS:
      return isSetMetrics();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ServerMetrics)
      return this.equals((ServerMetrics)that);
    return false;
  }

  public boolean equals(ServerMetrics that) {
    if (that == null)
      return false;

    boolean this_present_serverip = true && this.isSetServerip();
    boolean that_present_serverip = true && that.isSetServerip();
    if (this_present_serverip || that_present_serverip) {
      if (!(this_present_serverip && that_present_serverip))
        return false;
      if (!this.serverip.equals(that.serverip))
        return false;
    }

    boolean this_present_metrics = true;
    boolean that_present_metrics = true;
    if (this_present_metrics || that_present_metrics) {
      if (!(this_present_metrics && that_present_metrics))
        return false;
      if (this.metrics != that.metrics)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(ServerMetrics other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    ServerMetrics typedOther = (ServerMetrics)other;

    lastComparison = Boolean.valueOf(isSetServerip()).compareTo(typedOther.isSetServerip());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetServerip()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serverip, typedOther.serverip);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMetrics()).compareTo(typedOther.isSetMetrics());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMetrics()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.metrics, typedOther.metrics);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    org.apache.thrift.protocol.TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == org.apache.thrift.protocol.TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // SERVERIP
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.serverip = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // METRICS
          if (field.type == org.apache.thrift.protocol.TType.DOUBLE) {
            this.metrics = iprot.readDouble();
            setMetricsIsSet(true);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    // check for required fields of primitive type, which can't be checked in the validate method
    validate();
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.serverip != null) {
      oprot.writeFieldBegin(SERVERIP_FIELD_DESC);
      oprot.writeString(this.serverip);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldBegin(METRICS_FIELD_DESC);
    oprot.writeDouble(this.metrics);
    oprot.writeFieldEnd();
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ServerMetrics(");
    boolean first = true;

    sb.append("serverip:");
    if (this.serverip == null) {
      sb.append("null");
    } else {
      sb.append(this.serverip);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("metrics:");
    sb.append(this.metrics);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

}

