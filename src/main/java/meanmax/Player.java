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
import java.util.stream.Collectors;

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

record ManagerContext(Set<Unit> units, Map<UnitType, Map<Integer, Unit>> unitTypeRegistry, DistanceContext distanceContext, OptimizedContext optimizedContext) {
    static class Builder {

        private final Unit unit;
        private final Set<Unit> units;

        public Builder(final Unit unit, Set<Unit> units) {
            this.unit = unit;
            this.units = units;
        }

        public ManagerContext create() {
            Map<UnitType, Map<Integer, Unit>> unitTypeRegistry = getUnitTypeRegistry(units);
            DistanceContext distanceContext = getDistanceContext(unit, unitTypeRegistry);
            OptimizedContext optimizedContext = getOptimizedContext(unit, unitTypeRegistry, distanceContext);
            return new ManagerContext(units, unitTypeRegistry, distanceContext, optimizedContext);
        }

        private OptimizedContext getOptimizedContext(final Unit unit, final Map<UnitType, Map<Integer, Unit>> unitTypeRegistry, final DistanceContext distanceContext) {
            Map<UnitType, Map<Unit, Double>> unitTypeOptimizedWeightMap = new EnumMap<>(Map.ofEntries(
                    Map.entry(UnitType.Wreck, new HashMap<>()),
                    Map.entry(UnitType.Reaper, new HashMap<>()),
                    Map.entry(UnitType.Tanker, new HashMap<>()),
                    Map.entry(UnitType.Doof, new HashMap<>()),
                    Map.entry(UnitType.Destroyer, new HashMap<>())
            ));

            if (unit.getType() == UnitType.Reaper) {
                if (!distanceContext.unitTypeDistanceMap().get(UnitType.Wreck).isEmpty()) {
                    int sumDistance = distanceContext.unitTypeDistanceMap().get(UnitType.Wreck).stream().mapToInt(x -> x).sum();
                    int sumOfWater = unitTypeRegistry.get(UnitType.Wreck).values().stream().mapToInt(x -> x.getExtra()).sum();

                    for (var wreckEntry : unitTypeRegistry.get(UnitType.Wreck).entrySet()) {
                        double distanceWeight = (double) distanceContext.otherUnitDistanceMap().get(wreckEntry.getValue()) / (double) sumDistance;
                        double waterWeight = 1.0 - (double) wreckEntry.getValue().getExtra() / (double) sumOfWater;
                        unitTypeOptimizedWeightMap.get(UnitType.Wreck).put(wreckEntry.getValue(), distanceWeight + waterWeight);
                    }
                }

                if (!distanceContext.unitTypeDistanceMap().get(UnitType.Reaper).isEmpty()) {
                    int sumDistance = distanceContext.unitTypeDistanceMap().get(UnitType.Reaper).stream().mapToInt(x -> x).sum();

                    for (var reaperEntry : unitTypeRegistry.get(UnitType.Reaper).entrySet()) {
                        if (reaperEntry.getValue().getPlayerId() != Player.ID) {
                            double distanceWeight = (double) distanceContext.otherUnitDistanceMap().get(reaperEntry.getValue()) / (double) sumDistance;
                            unitTypeOptimizedWeightMap.get(UnitType.Reaper).put(reaperEntry.getValue(), distanceWeight);
                        }
                    }
                }

            }

            if (unit.getType() == UnitType.Destroyer) {
                if (!distanceContext.unitTypeDistanceMap().get(UnitType.Tanker).isEmpty()) {
                    int sumDistance = distanceContext.unitTypeDistanceMap().get(UnitType.Tanker).stream().mapToInt(x -> x).sum();
                    int sumOfWater = unitTypeRegistry.get(UnitType.Tanker).values().stream().mapToInt(x -> x.getExtra()).sum();

                    for (var wreckEntry : unitTypeRegistry.get(UnitType.Tanker).entrySet()) {
                        double distanceWeight = (double) distanceContext.otherUnitDistanceMap().get(wreckEntry.getValue()) / (double) sumDistance;
                        double waterWeight = 1.0 - (double) wreckEntry.getValue().getExtra() / (double) sumOfWater;
                        unitTypeOptimizedWeightMap.get(UnitType.Tanker).put(wreckEntry.getValue(), distanceWeight + waterWeight);
                    }
                }
            }

            if (unit.getType() == UnitType.Doof) {
                if (!distanceContext.unitTypeDistanceMap().get(UnitType.Reaper).isEmpty()) {
                    int sumDistance = distanceContext.unitTypeDistanceMap().get(UnitType.Reaper).stream().mapToInt(x -> x).sum();

                    for (var reaperEntry : unitTypeRegistry.get(UnitType.Reaper).entrySet()) {
                        if (reaperEntry.getValue().getPlayerId() != Player.ID) {
                            double distanceWeight = (double) distanceContext.otherUnitDistanceMap().get(reaperEntry.getValue()) / (double) sumDistance;
                            unitTypeOptimizedWeightMap.get(UnitType.Reaper).put(reaperEntry.getValue(), distanceWeight);
                        }
                    }
                }
            }

            return new OptimizedContext(unitTypeOptimizedWeightMap);
        }

        public Map<UnitType, Map<Integer, Unit>> getUnitTypeRegistry(Set<Unit> units) {
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

            for (Map.Entry<UnitType, Map<Integer, Unit>> unitType : unitRegistry.entrySet()) {
                for (Map.Entry<Integer, Unit> unit : unitType.getValue().entrySet()) {
                    if (!unit.getValue().equals(unitFor)) {
                        int distance = unitFor.getDistanceTo(unit.getValue());
                        otherUnitDistanceMap.put(unit.getValue(), distance);
                        distanceForOtherUnitMap.put(distance, unit.getValue());
                        unitTypeDistanceMap.get(unit.getValue().getType()).add(distance);
                    }
                }
            }

            Collections.sort(unitTypeDistanceMap.get(UnitType.Reaper));
            Collections.sort(unitTypeDistanceMap.get(UnitType.Wreck));
            Collections.sort(unitTypeDistanceMap.get(UnitType.Tanker));
            Collections.sort(unitTypeDistanceMap.get(UnitType.Destroyer));
            Collections.sort(unitTypeDistanceMap.get(UnitType.Doof));
            return new DistanceContext(otherUnitDistanceMap, distanceForOtherUnitMap, unitTypeDistanceMap);
        }
    }
}

record DistanceContext(Map<Unit, Integer> otherUnitDistanceMap, Map<Integer, Unit> distanceForOtherUnitMap, Map<UnitType, List<Integer>> unitTypeDistanceMap) { }

record OptimizedContext(Map<UnitType, Map<Unit, Double>> unitTypeOptimizedWeightMap) { }

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
            Unit doof = null;

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
                if (player == ID && unitTypeEnum == UnitType.Doof) {
                    doof = new Unit(unitId, unitTypeEnum, player, mass, radius, new Point(x, y), new Point(vx, vy), extra, extra2);
                }

                units.add(new Unit(unitId, unitTypeEnum, player, mass, radius, new Point(x, y), new Point(vx, vy), extra, extra2));

            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

//            ManagerContext reaperContext = new ManagerContext.Builder(reaper, units).create();
//            ManagerContext destroyerContext = new ManagerContext.Builder(destroyer, units).create();
//            ManagerContext doofContext = new ManagerContext.Builder(doof, units).create();
//
//            ReaperManager reaperManager = new ReaperManager(reaper, reaperContext);
//            DestroyerManager destroyerManager = new DestroyerManager(destroyer, destroyerContext);
//            DoofManager doofManager = new DoofManager(doof, doofContext);
//
//            reaperManager.update();
//            destroyerManager.update();
//            doofManager.update();

            System.out.println("WAIT");
            System.out.println("WAIT");
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

    public abstract void update();

    protected void stop() {
        System.out.println("WAIT");
    }

    protected void go(Point point, int throttle) {
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

    protected List<Map.Entry<Unit, Double>> getClosedOptimizedUnits(UnitType unitType) {
        if (context.optimizedContext()
                .unitTypeOptimizedWeightMap()
                .get(unitType).isEmpty()) {
            return new ArrayList<>();
        }

        return context.optimizedContext()
                .unitTypeOptimizedWeightMap()
                .get(unitType)
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

//        double minWeight = Double.MAX_VALUE;
//        Unit best = null;
//        for (var unitEntry : pointMap.entrySet()) {
//            minWeight = Math.min(unitEntry.getValue(), minWeight);
//            if (minWeight == unitEntry.getValue()) {
//                best = unitEntry.getKey();
//            }
//        }
//
//        return best;
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

    public void update() {
        Unit closestUnit = null;

        if (context.unitTypeRegistry().get(UnitType.Wreck).size() > 0) {
            List<Map.Entry<Unit, Double>> optimizedUnits = getClosedOptimizedUnits(UnitType.Wreck);
            closestUnit = optimizedUnits.get(0).getKey();
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
            go(closestUnit.getPosition(), MAX_SPEED * Math.min(context.distanceContext().otherUnitDistanceMap().get(closestUnit), SLOWDOWN_DISTANCE) / SLOWDOWN_DISTANCE);
        } else if (context.distanceContext().otherUnitDistanceMap().get(closestUnit) < SLOWDOWN_DISTANCE && context.distanceContext().otherUnitDistanceMap().get(closestUnit) > closestUnit.getRadius() - unit.getRadius() * 2) {
            go(closestUnit.getPosition(), AVERAGE_SPEED * Math.min(context.distanceContext().otherUnitDistanceMap().get(closestUnit), SLOWDOWN_DISTANCE) / SLOWDOWN_DISTANCE + 50);
        } else {
            stop();
        }
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

    public void update() {
        Unit closestUnit = null;

        if (context.unitTypeRegistry().get(UnitType.Tanker).size() > 0) {
            List<Map.Entry<Unit, Double>> optimizedUnits = getClosedOptimizedUnits(UnitType.Tanker);
            closestUnit = optimizedUnits.get(0).getKey();
        }

        System.err.println("****DESTROYER****");
        System.err.println(unit);
        System.err.println(closestUnit);
        System.err.println((context.distanceContext().otherUnitDistanceMap().get(closestUnit)));

        if (closestUnit == null) {
            stop();
        } else {
            go(closestUnit.getPosition(), MAX_SPEED);
        }

    }
}

class DoofManager extends BaseManager {
    public static final int MAX_WATER_TANK = 10;
    public static final int MIN_WATER_TANK = 1;
    private static final int SLOWDOWN_DISTANCE = 2000;
    private static final int MAX_SPEED = 300;
    private static final int AVERAGE_SPEED = 200;
    private static final double MASS = 1.5;
    private static final double FRICTION = 0.3;

    public DoofManager(Unit unit, final ManagerContext context) {
        super(unit, context);
    }

    public void update() {
        Unit closestUnit = null;

        if (context.unitTypeRegistry().get(UnitType.Reaper).size() > 0) {
            List<Map.Entry<Unit, Double>> optimizedUnits = getClosedOptimizedUnits(UnitType.Reaper);
            closestUnit = optimizedUnits.get(0).getKey();
        }

        System.err.println("****DOOF****");
        System.err.println(unit);
        System.err.println(closestUnit);
        System.err.println((context.distanceContext().otherUnitDistanceMap().get(closestUnit)));

        if (closestUnit == null) {
            stop();
        } else {
            go(closestUnit.getPosition(), MAX_SPEED);
        }

    }
}
