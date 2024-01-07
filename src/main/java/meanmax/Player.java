import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

enum UnitType {
    Reaper(0),
    Destroyer(1),
    Doof(2),
    Tanker(3),
    Wreck(4);
    private final int type;

    UnitType(int type) {
        this.type = type;
    }

    public static UnitType fromInt(int x) {
        return switch (x) {
            case 0 -> Reaper;
            case 1 -> Destroyer;
            case 2 -> Doof;
            case 3 -> Tanker;
            case 4 -> Wreck;
            default -> throw new RuntimeException("Wrong type: " + x);
        };
    }

    public int get() {
        return type;
    }
}

record ManagerContext(Set<Unit> units, Map<UnitType, Map<Integer, Unit>> unitTypeRegistry, DistanceContext distanceContext) {
    static class Builder {

        private final Unit unit;
        private final Set<Unit> units;

        public Builder(final Unit unit, Set<Unit> units) {
            this.unit = unit;
            this.units = units;
        }

        public ManagerContext create() {
            Map<UnitType, Map<Integer, Unit>> unitTypeRegistry = countUnitTypeRegistry(units);
            DistanceContext distanceContext = getDistanceContext(unit, unitTypeRegistry);
            return new ManagerContext(units, unitTypeRegistry, distanceContext);
        }

        public Map<UnitType, Map<Integer, Unit>> countUnitTypeRegistry(Set<Unit> units) {
            Map<UnitType, Map<Integer, Unit>> unitTypeRegistry = new EnumMap<>(Map.ofEntries(
                    Map.entry(UnitType.Reaper, new HashMap<>()),
                    Map.entry(UnitType.Destroyer, new HashMap<>()),
                    Map.entry(UnitType.Doof, new HashMap<>()),
                    Map.entry(UnitType.Tanker, new HashMap<>()),
                    Map.entry(UnitType.Wreck, new HashMap<>())
            ));

            for (var unit : units) {
                unitTypeRegistry.get(unit.getType()).put(unit.getId(), unit);
            }

            return unitTypeRegistry;
        }

        public DistanceContext getDistanceContext(Unit unitFor, Map<UnitType, Map<Integer, Unit>> unitRegistry) {
            Map<Unit, Integer> otherUnitDistanceMap = new HashMap<>();
            Map<Integer, Unit> distanceForOtherUnitMap = new HashMap<>();
            Map<UnitType, List<Integer>> unitTypeDistanceMap = new EnumMap<>(Map.ofEntries(
                    Map.entry(UnitType.Reaper, new ArrayList<>()),
                    Map.entry(UnitType.Destroyer, new ArrayList<>()),
                    Map.entry(UnitType.Doof, new ArrayList<>()),
                    Map.entry(UnitType.Tanker, new ArrayList<>()),
                    Map.entry(UnitType.Wreck, new ArrayList<>())
            ));
            List<Integer> allAscSortedDistance = new ArrayList<>();
            for (Map.Entry<UnitType, Map<Integer, Unit>> unitType : unitRegistry.entrySet()) {
                for (Map.Entry<Integer, Unit> unit : unitType.getValue().entrySet()) {
                    if (!unit.getValue().equals(unitFor)) {
                        int distance = unitFor.getDistanceTo(unit.getValue());
                        otherUnitDistanceMap.put(unit.getValue(), distance);
                        distanceForOtherUnitMap.put(distance, unit.getValue());
                        unitTypeDistanceMap.get(unit.getValue().getType()).add(distance);
                        allAscSortedDistance.add(distance);
                    }
                }
            }

            Collections.sort(allAscSortedDistance);
            Collections.sort(unitTypeDistanceMap.get(UnitType.Reaper));
            Collections.sort(unitTypeDistanceMap.get(UnitType.Wreck));
            return new DistanceContext(otherUnitDistanceMap, distanceForOtherUnitMap, unitTypeDistanceMap, allAscSortedDistance);
        }
    }
}

record DistanceContext(Map<Unit, Integer> otherUnitDistanceMap, Map<Integer, Unit> distanceForOtherUnitMap, Map<UnitType, List<Integer>> unitTypeDistanceMap, List<Integer> allAscSortedDistance) { }

class Unit {
    private int id;
    private UnitType type;
    private int playerId;
    private float mass;
    private int radius;
    private Point position;
    private Point speed;
    private int extra;
    private int extra2;

    public Unit(final int id, final UnitType type, final int playerId, final float mass, final int radius, final Point position, final Point speed, final int extra, final int extra2) {
        this.id = id;
        this.type = type;
        this.playerId = playerId;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.speed = speed;
        this.extra = extra;
        this.extra2 = extra2;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getExtra() {
        return extra;
    }

    public int getRadius() {
        return radius;
    }

    public int getId() {
        return id;
    }

    public UnitType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Unit unit = (Unit) o;
        return id == unit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", type=" + type +
                ", playerId=" + playerId +
                ", mass=" + mass +
                ", radius=" + radius +
                ", position=" + position +
                ", speed=" + speed +
                ", extra=" + extra +
                ", extra2=" + extra2 +
                '}';
    }

    public int getDistanceTo(Unit other) {
        return (int) position.distance(other.position);
    }
}

class Player {

    public static final int ID = 0;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            Set<Unit> units = new HashSet<>();
            Unit reaper = null;
            Unit destroyer = null;

            int myScore = in.nextInt();
            int enemyScore1 = in.nextInt();
            int enemyScore2 = in.nextInt();
            int myRage = in.nextInt();
            int enemyRage1 = in.nextInt();
            int enemyRage2 = in.nextInt();
            int unitCount = in.nextInt();
            for (int i = 0; i < unitCount; i++) {
                int unitId = in.nextInt();
                int unitType = in.nextInt();
                int player = in.nextInt();
                float mass = in.nextFloat();
                int radius = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int vx = in.nextInt();
                int vy = in.nextInt();
                int extra = in.nextInt();
                int extra2 = in.nextInt();

                UnitType unitTypeEnum = UnitType.fromInt(unitType);
                if (player == ID && unitTypeEnum == UnitType.Reaper) {
                    reaper = new Unit(unitId, unitTypeEnum, player, mass, radius, new Point(x, y), new Point(vx, vy), extra, extra2);
                }
                if (player == ID && unitTypeEnum == UnitType.Destroyer) {
                    destroyer = new Unit(unitId, unitTypeEnum, player, mass, radius, new Point(x, y), new Point(vx, vy), extra, extra2);
                }

                units.add(new Unit(unitId, unitTypeEnum, player, mass, radius, new Point(x, y), new Point(vx, vy), extra, extra2));

            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            ManagerContext reaperContext = new ManagerContext.Builder(reaper, units).create();
            ManagerContext destroyerContext = new ManagerContext.Builder(destroyer, units).create();
            ReaperManager reaperManager = new ReaperManager(reaper, reaperContext);
            DestroyerManager destroyerManager = new DestroyerManager(destroyer, destroyerContext);

            reaperManager.process();
            destroyerManager.process();

//            System.out.println(unit.getPosition().x + " " + unit.getPosition().y + " " + 300);
//            System.out.println("WAIT");
            System.out.println("WAIT");
        }
    }
}

abstract class BaseManager {
    protected final Unit unit;
    protected final ManagerContext context;

    public BaseManager(Unit unit, ManagerContext context) {
        this.unit = unit;
        this.context = context;
    }

    protected void stop() {
        System.out.println("WAIT");
    }

    protected void update(Point point, int throttle) {
        System.out.println(point.x + " " + point.y + " " + throttle);
    }

    protected Unit getFirstClosestUnitByType(UnitType unitType, DistanceContext context) {
        return getUnitByTypeAndIndex(0, unitType, context);
    }

    protected Unit getUnitByTypeAndIndex(int index, UnitType unitType, DistanceContext context) {
        if (index >= context.unitTypeDistanceMap().get(unitType).size() || context.unitTypeDistanceMap().get(unitType).size() == 0) {
            return null;
        }

        return context.distanceForOtherUnitMap().get(
                context.unitTypeDistanceMap().get(unitType).get(index)
        );
    }

    protected Unit getUnitByTypeAndQuery(UnitType unitType, ManagerContext context, Predicate<Unit> filter) {
        return context.unitTypeRegistry()
                .get(unitType)
                .entrySet().stream()
                .map(x -> x.getValue())
                .filter(filter)
                .findFirst()
                .get();
    }

    protected Unit getBestPoint(UnitType unitType) {
        int sumDistance = context.distanceContext().unitTypeDistanceMap().get(unitType).stream().mapToInt(x -> x).sum();
        int sumOfWater = context.unitTypeRegistry().get(unitType).values().stream().mapToInt(x -> x.getExtra()).sum();
        HashMap<Unit, Double> pointMap = new HashMap<>();
        for (var unitEntry : context.unitTypeRegistry().get(unitType).entrySet()) {
            double distanceWeight = (double) context.distanceContext().otherUnitDistanceMap().get(unitEntry.getValue()) / (double) sumDistance;
            double waterWeight = 1.0 - (double) unitEntry.getValue().getExtra() / (double) sumOfWater;
            pointMap.put(unitEntry.getValue(), distanceWeight + waterWeight);
        }

        double minWeight = Double.MAX_VALUE;
        Unit best = null;
        for (var unitEntry : pointMap.entrySet()) {
            minWeight = Math.min(unitEntry.getValue(), minWeight);
            if (minWeight == unitEntry.getValue()) {
                best = unitEntry.getKey();
            }
        }

        return best;
    }

    public Unit getFirstCloseUnitByType2(UnitType unitType, Map<Unit, Integer> unitDistances, final Map<UnitType, Map<Integer, Unit>> unitTypeRegistry) {
        int minDist = Integer.MAX_VALUE;
        for (var unitDist : unitDistances.entrySet()) {
            if (unitDist.getKey().getType() == unitType) {
                minDist = Math.min(unitDist.getValue(), minDist);
            }
        }

        for (Map.Entry<Integer, Unit> unitEntry : unitTypeRegistry.get(unitType).entrySet()) {
            if (unitDistances.get(unitEntry.getValue()) == minDist) {
                return unitEntry.getValue();
            }
        }

        return null;
    }
}

class ReaperManager extends BaseManager {
    private static final int SLOWDOWN_DISTANCE = 3000;
    private static final int MAX_SPEED = 300;
    private static final int AVERAGE_SPEED = 200;
    private static final double MASS = 1;
    private static final double FRICTION = 0.2;

    public ReaperManager(Unit unit, final ManagerContext context) {
        super(unit, context);
    }

    public void process() {
        Unit closestUnit = null;

        if (context.unitTypeRegistry().get(UnitType.Wreck).size() > 0) {
            closestUnit = getBestPoint(UnitType.Wreck);
        }

        if (closestUnit == null) {
            closestUnit = getUnitByTypeAndQuery(UnitType.Destroyer, context, x -> x.getPlayerId() == Player.ID);
        }

        System.err.println("****REAPER****");
        System.err.println(unit);
        System.err.println(closestUnit);
        System.err.println((context.distanceContext().otherUnitDistanceMap().get(closestUnit)));

        //        if (context.distanceContext().otherUnitDistanceMap().get(closestReaper) <= reaper.getRadius() * 3 + closestReaper.getRadius() * 2) {
//            Unit longestWreck = getUnitByTypeAndIndex(1, UnitType.Wreck, context.distanceContext());
//            update(longestWreck.getPosition(), MAX_SPEED * Math.min(context.distanceContext().otherUnitDistanceMap().get(closestWreck), SLOWDOWN_DISTANCE) / SLOWDOWN_DISTANCE + 50);
//        } else
        if (context.distanceContext().otherUnitDistanceMap().get(closestUnit) >= SLOWDOWN_DISTANCE) {
            update(closestUnit.getPosition(), MAX_SPEED * Math.min(context.distanceContext().otherUnitDistanceMap().get(closestUnit), SLOWDOWN_DISTANCE) / SLOWDOWN_DISTANCE);
        } else if (context.distanceContext().otherUnitDistanceMap().get(closestUnit) < SLOWDOWN_DISTANCE && context.distanceContext().otherUnitDistanceMap().get(closestUnit) > closestUnit.getRadius() - unit.getRadius() * 2) {
            update(closestUnit.getPosition(), AVERAGE_SPEED * Math.min(context.distanceContext().otherUnitDistanceMap().get(closestUnit), SLOWDOWN_DISTANCE) / SLOWDOWN_DISTANCE + 50);
        } else {
            stop();
        }
    }

    private Unit findWreckWithMaxWater(final int water) {
        for (int i = 0; i < context.unitTypeRegistry().get(UnitType.Wreck).size(); i++) {
            Unit localUnit = getUnitByTypeAndIndex(i, UnitType.Wreck, context.distanceContext());
            if (localUnit.getExtra() > water || context.distanceContext().otherUnitDistanceMap().get(localUnit) <= SLOWDOWN_DISTANCE) {
                return localUnit;
            }
        }

        return null;
    }

}

class DestroyerManager extends BaseManager {
    public static final int MAX_WATER_TANK = 10;
    public static final int MIN_WATER_TANK = 1;
    private static final int SLOWDOWN_DISTANCE = 2000;
    private static final int MAX_SPEED = 300;
    private static final int AVERAGE_SPEED = 200;
    private static final double MASS = 1.5;
    private static final double FRICTION = 0.3;

    public DestroyerManager(Unit unit, final ManagerContext context) {
        super(unit, context);
    }

    public void process() {

        Unit closestUnit = null;

        if (context.unitTypeRegistry().get(UnitType.Tanker).size() > 0) {
            closestUnit = getBestPoint(UnitType.Tanker);
        }

        System.err.println("****DESTROYER****");
        System.err.println(unit);
        System.err.println(closestUnit);
        System.err.println((context.distanceContext().otherUnitDistanceMap().get(closestUnit)));

        if (closestUnit == null) {
            stop();
        } else {
            update(closestUnit.getPosition(), MAX_SPEED);
        }

    }

    private Unit findTankWithMaxWater(final int water) {
        for (int i = 0; i < context.unitTypeRegistry().get(UnitType.Tanker).size(); i++) {
            Unit localUnit = getUnitByTypeAndIndex(i, UnitType.Tanker, context.distanceContext());
            if (localUnit.getExtra() > water || context.distanceContext().otherUnitDistanceMap().get(localUnit) <= SLOWDOWN_DISTANCE) {
                return localUnit;
            }
        }

        return null;
    }
}
