package hackercup.hackercup2022.round2.perfectlybalanced;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum QueryType {
    SET(1),
    GET(2);

    private static final Map<Integer, QueryType> QUERY_TYPE_MAP = Arrays.stream(QueryType.values())
            .collect(Collectors.toMap(QueryType::getType, Function.identity()));
    private final int type;
    QueryType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static QueryType fromType(int type) {
        return QUERY_TYPE_MAP.get(type);
    }
}
