package ru.nsu.maltsev.task_2_3_1.snake.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameModel {
    private final GameConfig config;
    private final Random random;

    private final Deque<Position> snake;
    private final Set<Position> snakeCells;
    private final Set<Position> obstacles;
    private final List<Food> foods;

    private Direction direction;
    private Direction nextDirection;
    private GameStatus status;

    private int score;
    private int level;
    private int tickMillis;
    private int growth;
    private boolean victoryAchieved;

    public GameModel(GameConfig config) {
        this(config, new Random());
    }

    public GameModel(GameConfig config, Random random) {
        this.config = config;
        this.random = random;

        snake = new ArrayDeque<>();
        snakeCells = new HashSet<>();
        obstacles = new HashSet<>();
        foods = new ArrayList<>();

        reset();
    }

    public void reset() {
        snake.clear();
        snakeCells.clear();
        obstacles.clear();
        foods.clear();

        Position start = new Position(config.getRows() / 2, config.getColumns() / 2);
        snake.addFirst(start);
        snakeCells.add(start);

        direction = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        status = GameStatus.RUNNING;

        score = 0;
        level = 1;
        tickMillis = config.getInitialTickMillis();
        growth = 0;
        victoryAchieved = false;

        generateObstacles();
        refillFood();
    }

    public void step() {
        if (status != GameStatus.RUNNING) {
            return;
        }

        direction = nextDirection;

        Position currentHead = snake.peekFirst();
        Position newHead = currentHead.move(direction);
        Food eatenFood = findFood(newHead);

        if (isCollision(newHead, eatenFood != null)) {
            status = GameStatus.LOST;
            return;
        }

        snake.addFirst(newHead);
        snakeCells.add(newHead);

        if (eatenFood != null) {
            foods.remove(eatenFood);
            score += eatenFood.getType().getScore();
            growth += eatenFood.getType().getGrowth();
            refillFood();
        }

        if (growth > 0) {
            growth--;
        } else {
            Position removedTail = snake.removeLast();
            snakeCells.remove(removedTail);
        }

        updateLevel();

        if (snake.size() >= config.getWinLength()) {
            victoryAchieved = true;
        }
    }

    public void setDirection(Direction newDirection) {
        if (snake.size() == 1 || !newDirection.isOpposite(direction)) {
            nextDirection = newDirection;
        }
    }

    private boolean isCollision(Position position, boolean willGrow) {
        if (!isInsideBoard(position)) {
            return true;
        }

        if (obstacles.contains(position)) {
            return true;
        }

        Position tail = snake.peekLast();
        boolean movingToFreeTail = position.equals(tail) && !willGrow;

        return snakeCells.contains(position) && !movingToFreeTail;
    }

    private boolean isInsideBoard(Position position) {
        return position.getRow() >= 0
                && position.getRow() < config.getRows()
                && position.getColumn() >= 0
                && position.getColumn() < config.getColumns();
    }

    private Food findFood(Position position) {
        for (Food food : foods) {
            if (food.getPosition().equals(position)) {
                return food;
            }
        }

        return null;
    }

    private void generateObstacles() {
        for (int i = 0; i < config.getObstacleCount(); i++) {
            Position position = randomFreePosition();

            if (position == null) {
                return;
            }

            obstacles.add(position);
        }
    }

    private void refillFood() {
        while (foods.size() < config.getFoodCount()) {
            Position position = randomFreePosition();

            if (position == null) {
                return;
            }

            foods.add(new Food(position, FoodType.REGULAR));
        }
    }

    private Position randomFreePosition() {
        List<Position> freePositions = new ArrayList<>();

        for (int row = 0; row < config.getRows(); row++) {
            for (int column = 0; column < config.getColumns(); column++) {
                Position position = new Position(row, column);

                if (isFree(position)) {
                    freePositions.add(position);
                }
            }
        }

        if (freePositions.isEmpty()) {
            return null;
        }

        return freePositions.get(random.nextInt(freePositions.size()));
    }

    private boolean isFree(Position position) {
        return !snakeCells.contains(position)
                && !obstacles.contains(position)
                && findFood(position) == null;
    }

    private void updateLevel() {
        int newLevel = 1 + (snake.size() - 1) / config.getLevelLengthStep();

        if (newLevel != level) {
            level = newLevel;
            tickMillis = Math.max(
                    config.getMinTickMillis(),
                    config.getInitialTickMillis() - (level - 1) * config.getSpeedStepMillis()
            );
        }
    }

    public GameConfig getConfig() {
        return config;
    }

    public List<Position> getSnake() {
        return Collections.unmodifiableList(new ArrayList<>(snake));
    }

    public Set<Position> getObstacles() {
        return Collections.unmodifiableSet(obstacles);
    }

    public List<Food> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getTickMillis() {
        return tickMillis;
    }

    public boolean isVictoryAchieved() {
        return victoryAchieved;
    }
}