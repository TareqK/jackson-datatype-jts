package me.busr.jts.geojson.parsers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import static me.busr.jts.geojson.common.GeoJson.COORDINATES;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

/**
 * Created by mihaildoronin on 11/11/15.
 */
public class MultiLineStringParser extends BaseParser implements GeometryParser<MultiLineString> {

    public MultiLineStringParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public MultiLineString multiLineStringFromJson(JsonNode root) {
        return geometryFactory.createMultiLineString(
                lineStringsFromJson(root.get(COORDINATES)));
    }

    private LineString[] lineStringsFromJson(JsonNode array) {
        LineString[] strings = new LineString[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            strings[i] = geometryFactory.createLineString(PointParser.coordinatesFromJson(array.get(i)));
        }
        return strings;
    }

    @Override
    public MultiLineString geometryFromJson(JsonNode node) throws JsonMappingException {
        return multiLineStringFromJson(node);
    }
}
