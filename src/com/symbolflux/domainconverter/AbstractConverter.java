package com.symbolflux.domainconverter;

import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * Created by weston on 12/12/14.
 */
public abstract class AbstractConverter<I, F> {
    //The linked Domains
    private Domain source;
    private Domain dest;

    //Queues of work completed by the source domain before requested by the destination domain.
    private Queue<F> convertedObjectBuffer;
    private Queue<ArrayList<F>> convertedObjectSetBuffer;

    //These maps are used to implement persistent links between objects in source and destination domains.
    //Sometimes it's desirable to to have two Converters linked, acting as inverses of one another; in that
    //case, it's beneficial for the two to share a persistent map, though the keys for one will be the
    //values of the other, etc. Only one or the other is ever active at a given time, though:
    //if one converter is constructed as the inverse of another, its 'map' object will be null, and its
    //'inverseMap' object will be used. If it's desired that final objects are constructed from scratch
    //each time, 'usePersistentMap' should be false, and neither map nor inverseMap will be used.
    private boolean usePersistentMap;
    private boolean useInverseMap;
    private HashBiMap<I, F> map;
    private HashBiMap<F, I> inverseMap;

    public AbstractConverter attachDomains(Domain source, Domain dest, boolean usePersistentMap) {
        this.source = source;
        this.dest= dest;

        this.convertedObjectBuffer = new LinkedList<F>();
        this.convertedObjectSetBuffer = new LinkedList<ArrayList<F>>();

        this.usePersistentMap = usePersistentMap;
        if (this.usePersistentMap)
            map = HashBiMap.create();

        return this;
    }

    public AbstractConverter attachDomains(Domain source, Domain dest, AbstractConverter<F, I> inverse) {
        this.attachDomains(source, dest, true);

        assert inverse.usePersistentMap : "Doesn't make sense to be the inverse of a converter not using a persistent map";

        this.map = null;
        this.inverseMap = inverse.map;
        this.useInverseMap = true;

        return this;
    }

    private F getFinalFromInitial(I initialObject) {
        F finalObject;

        //Grab the mapped object fom our persistent map, if applicable
        Optional<F> optFinalObject = Optional.empty();
        if (this.usePersistentMap)
            optFinalObject = Optional.ofNullable(this.getMappedObject(initialObject));

        if (optFinalObject.isPresent()) { //We found a corresponding final object in the map
            if (initialObject instanceof Modifiable) {
                Modifiable maybeModifiedInitialObject = (Modifiable)initialObject;
                if (maybeModifiedInitialObject.modified()) { //The initial object was modified
                    F updatedFinalObject = this.update(initialObject, optFinalObject.get());
                    maybeModifiedInitialObject.setUnmodified();

                    finalObject =  updatedFinalObject;
                } else { //It wasn't modified, so just return the mapped object
                    finalObject =  optFinalObject.get();
                }
            } else { //The object is not a modifiable, so create a new final object
                finalObject =  this.convert(initialObject);
            }
        } else { //There was no mapped final object, so create a new final object
            finalObject = this.convert(initialObject);
        }

        if (this.usePersistentMap)
            this.putMappedObjects(initialObject, finalObject);

        return finalObject;
    }

    /**
     * Abstracts whether the normal or inverse persistent map is used during a 'put' operation
     * @param initialObject
     * @param finalObject
     */
    public void putMappedObjects(I initialObject, F finalObject) {
        assert this.usePersistentMap : "putMappedObject should only be used when 'usePersistentMap' is true";

        if (this.useInverseMap)
            this.inverseMap.inverse().put(initialObject, finalObject);
        else
            this.map.put(initialObject, finalObject);
    }

    /**
     * Abstracts whether the normal or inverse persistent map is used during a 'get' operation
     * @param initialObject
     * @return
     */
    public F getMappedObject(I initialObject) {
        assert this.usePersistentMap : "getMappedObject should only be used when 'usePersistentMap' is true";

        if (this.useInverseMap)
            return this.inverseMap.inverse().get(initialObject);
        else
            return this.map.get(initialObject);
    }

    //Implementations should construct a new final object from the given initial object
    public abstract F convert(I initialObject);

    //Implementations should update the final object to reflect the current state of the given initial object
    public abstract F update(I changedInitialObject, F oldFinalObject);

    public void push(I initialObject) {
        F finalObject = this.getFinalFromInitial(initialObject);
        dest.pushOccurred(this, finalObject);
    }

    public void pushSet(I[] initialObjects) {
        ArrayList<F> finalObjects = new ArrayList<F>();

        for (I initialObject : initialObjects) {
            finalObjects.add(this.getFinalFromInitial(initialObject));
        }

        dest.pushSetOccurred(this, finalObjects);
    }

    public F pull() {
        if (!this.convertedObjectBuffer.isEmpty()) //if the source domain already has a result buffered, grab it
            return this.convertedObjectBuffer.remove();

        I pulledObject = source.pullOccurred(this);
        return this.getFinalFromInitial(pulledObject);
    }

    public ArrayList<F> pullSet() {
        if (!this.convertedObjectSetBuffer.isEmpty()) //if the source domain already has a result buffered, grab it
            return this.convertedObjectSetBuffer.remove();

        ArrayList<I> pulledObjects = source.pullSetOccurred(this);
        ArrayList<F> finalObjects = new ArrayList<F>();

        //potentially a way of doing it with Streams...
//        pulledObjects.stream().collect(Collectors.mapping((I element) -> {return this.getFinalFromInitial(element);}, Collectors.toList()));

        for (I initialObject : pulledObjects) {
            finalObjects.add(this.getFinalFromInitial(initialObject));
        }

        return finalObjects;
    }

    /**
     * If a source Domain would like to do some work before asked, it can use this to buffer the results
     * @param initialObject
     */
    public void buffer(I initialObject) {
        F finalObject = this.getFinalFromInitial(initialObject);
        this.convertedObjectBuffer.add(finalObject);
    }

    /**
     * This is the 'set' version of {@link #buffer(I)}
     * @param initialObjects
     */
    public void bufferSet(I[] initialObjects) {
        ArrayList<F> finalObjects = new ArrayList<F>();

        for (I initialObject : initialObjects) {
            finalObjects.add(this.getFinalFromInitial(initialObject));
        }

        this.convertedObjectSetBuffer.add(finalObjects);
    }
}
