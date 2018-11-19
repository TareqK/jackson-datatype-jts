package me.busr.jts.geojson.parsers;

import static me.busr.jts.geojson.common.GeoJson.GEOMETRY_COLLECTION;
import static me.busr.jts.geojson.common.GeoJson.LINE_STRING;
import static me.busr.jts.geojson.common.GeoJson.MULTI_LINE_STRING;
import static me.busr.jts.geojson.common.GeoJson.MULTI_POINT;
import static me.busr.jts.geojson.common.GeoJson.MULTI_POLYGON;
import static me.busr.jts.geojson.common.GeoJson.POINT;
import static me.busr.jts.geojson.common.GeoJson.POLYGON;
import static me.busr.jts.geojson.common.GeoJson.TYPE;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;


/**
 * Created by mihaildoronin on 11/11/15.
 */
public class GenericGeometryParser extends BaseParser implements GeometryParser<Geometry> {

    private Map<String, GeometryParser> parsers;

    public GenericGeometryParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
        parsers = new HashMap<String, GeometryParser>();
        parsers.put(POINT, new PointParser(geometryFactory));
        parsers.put(MULTI_POINT, new MultiPointParser(geometryFactory));
        parsers.put(LINE_STRING, new LineStringParser(geometryFactory));
        parsers.put(MULTI_LINE_STRING, new MultiLineStringParser(geometryFactory));
        parsers.put(POLYGON, new PolygonParser(geometryFactory));
        parsers.put(MULTI_POLYGON, new MultiPolygonParser(geometryFactory));
        parsers.put(GEOMETRY_COLLECTION, new GeometryCollectionParser(geometryFactory, this));
    }

    @Override
    public Geometry geometryFromJson(JsonNode node) throws JsonMappingException {
        String typeName = node.get(TYPE).asText();
        GeometryParser parser = parsers.get(typeName);
        if (parser != null) {
            return parser.geometryFromJson(node);
        }
        else {
            throw new JsonMappingException("Invalid geometry type: " + typeName);
        }
    }
}
