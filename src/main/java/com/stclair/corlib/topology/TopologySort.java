package com.stclair.corlib.topology;

import java.util.List;
import java.util.function.Supplier;

public interface TopologySort<T> {

    <X> List<X> sort(X start, Navigator<X> navigator);

    List<T> sort(T start);

    <X> TopologySort<X> setNavigator(Navigator<X> navigator);

    void setNodeFirst(boolean nodeFirst);

    void setSuppressRepeats(boolean suppressRepeats);
}
