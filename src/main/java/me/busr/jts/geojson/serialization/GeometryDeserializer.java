package me.busr.jts.geojson.serialization;

import me.busr.jts.geojson.parsers.GeometryParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import me.busr.jts.geojson.common.GeoJson;
import me.busr.jts.geojson.parsers.GenericGeometryParser;
import me.busr.jts.geojson.parsers.GeometryCollectionParser;
import me.busr.jts.geojson.parsers.LineStringParser;
import me.busr.jts.geojson.parsers.MultiLineStringParser;
import me.busr.jts.geojson.parsers.MultiPointParser;
import me.busr.jts.geojson.parsers.MultiPolygonParser;
import me.busr.jts.geojson.parsers.PointParser;
import me.busr.jts.geojson.parsers.PolygonParser;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GeometryDeserializer extends StdDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode root = oc.readTree(jsonParser);
        GeometryParser geometryParser;
        GeometryFactory factory = new GeometryFactory();
        switch (root.get(GeoJson.TYPE).asText()) {
            case GeoJson.POINT:
                geometryParser = new PointParser(factory);
                break;
            case GeoJson.LINE_STRING:
                geometryParser = new LineStringParser(factory);
                break;
            case GeoJson.POLYGON:
                geometryParser = new PolygonParser(factory);
                break;
            case GeoJson.MULTI_POINT:
                geometryParser = new MultiPointParser(factory);
                break;
            case GeoJson.MULTI_LINE_STRING:
                geometryParser = new MultiLineStringParser(factory);
                break;
            case GeoJson.MULTI_POLYGON:
                geometryParser = new MultiPolygonParser(factory);
                break;
            case GeoJson.GEOMETRY_COLLECTION:
                geometryParser = new GeometryCollectionParser(factory, new GenericGeometryParser(factory));
                break;
            case GeoJson.GEOMETRIES:
                geometryParser = new GenericGeometryParser(factory);
                break;
            default:
                throw new IOException();

        }
        return geometryParser.geometryFromJson(root);
    }

    public GeometryDeserializer(Class<?> vc) {
        super(vc);
    }

    public GeometryDeserializer(JavaType valueType) {
        super(valueType);
    }

    public GeometryDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public GeometryDeserializer() {
        super(Geometry.class);
    }
}
