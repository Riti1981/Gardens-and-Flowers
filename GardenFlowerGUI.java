import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Flower {
    private String name;
    private String description;
    private boolean watered;

    public Flower(String name, String description) {
        this.name = name;
        this.description = description;
        this.watered = false;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isWatered() { return watered; }
    public void water() { watered = true; }
}

class GameModel {
    private ArrayList<Flower> flowers;
    private ArrayList<Flower> bouquetFlowers;
    private int currentFlowerIndex;

    public GameModel() {
        flowers = new ArrayList<>();
        bouquetFlowers = new ArrayList<>();
        currentFlowerIndex = 0;
        loadFlowers();
    }

    private void loadFlowers() {
        flowers.add(new Flower("California Poppy",
                "Bright orange petals fading into yellow, cup-shaped. Found in West Coast/Southwest US. " +
                "Grows in sandy soil, full sun. Easy to grow and self-seeds."));

        flowers.add(new Flower("Yarrow",
                "Feathery foliage with white flowers (May–June). Great for pollinators, drought-tolerant, " +
                "helps erosion control, and uses less water."));

        flowers.add(new Flower("Sunflower",
                "Bright yellow petals with a brown center. Very drought-tolerant, attracts pollinators, " +
                "can grow year-round."));

        flowers.add(new Flower("Blue Flax",
                "Rare true blue flower with 5 petals. Drought-tolerant, eco-friendly, supports pollinators, " +
                "helps with erosion control."));

        flowers.add(new Flower("Chuparosa",
                "Shrub with many red flowers. Attracts hummingbirds and butterflies. " +
                "Drought-tolerant, supports ecosystems, helps water conservation."));

        flowers.add(new Flower("Arroyo Lupine",
                "Purple-blue spike flowers. Fixes nitrogen in soil, supports butterflies and moths, " +
                "grows in many soil types."));

        flowers.add(new Flower("Winecup Clarkia",
                "Light pink/lavender petals with dark center. Found across California (except desert). " +
                "Small annual flower that attracts pollinators."));
    }

    public ArrayList<Flower> getFlowers() { return flowers; }
    public ArrayList<Flower> getBouquetFlowers() { return bouquetFlowers; }

    public Flower getCurrentFlower() {
        if (currentFlowerIndex < flowers.size()) return flowers.get(currentFlowerIndex);
        return null;
    }

    public int getCurrentFlowerIndex() { return currentFlowerIndex; }

    public void waterNextFlower() {
        if (currentFlowerIndex < flowers.size()) {
            flowers.get(currentFlowerIndex).water();
            currentFlowerIndex++;
        }
    }

    public boolean allFlowersWatered() {
        return currentFlowerIndex >= flowers.size();
    }

    public void addToBouquet(Flower flower) {
        if (!bouquetFlowers.contains(flower)) bouquetFlowers.add(flower);
    }

    public void clearBouquet() {
        bouquetFlowers.clear();
    }
}

class GardenPanel extends JPanel {
    private GameModel model;
    private boolean showCan;

    public GardenPanel(GameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(700, 400));
        setBackground(new Color(210, 240, 210));
    }

    public void showCan() { showCan = true; repaint(); }
    public void hideCan() { showCan = false; repaint(); }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Flower> flowers = model.getFlowers();
        int groundY = 300;
        int w = getWidth() / flowers.size();

        g.setColor(new Color(110, 70, 35));
        g.fillRect(0, groundY, getWidth(), 80);

        for (int i = 0; i < flowers.size(); i++) {
            int x = i * w + w / 2;

            g.setColor(Color.GREEN);
            g.fillRect(x - 5, groundY - 70, 10, 70);

            if (flowers.get(i).isWatered()) {
                g.setColor(Color.PINK);
                g.fillOval(x - 15, groundY - 90, 30, 30);
            } else {
                g.setColor(Color.GREEN.darker());
                g.fillOval(x - 10, groundY - 50, 20, 10);
            }

            g.setColor(Color.BLACK);
            g.drawString(flowers.get(i).getName(), x - 40, groundY + 20);
        }

        if (showCan && !model.allFlowersWatered()) {
            int index = model.getCurrentFlowerIndex();
            int x = index * w + w / 2;

            g.setColor(Color.BLUE);
            g.fillRect(x - 30, 60, 60, 30);
            g.drawString("Watering", x - 30, 50);
        }

        if (model.allFlowersWatered()) {
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("All flowers have bloomed!", 200, 50);
        }
    }
}

class BouquetPanel extends JPanel {
    private GameModel model;

    public BouquetPanel(GameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(300, 400));
        setBackground(new Color(255, 245, 230));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(230, 200, 160));
        g.fillRect(60, 100, 180, 200);

        int x = 90;
        int y = 130;

        for (Flower f : model.getBouquetFlowers()) {
            g.setColor(Color.PINK);
            g.fillOval(x, y, 20, 20);
            g.drawString(f.getName(), x - 10, y + 40);

            x += 40;
            if (x > 200) {
                x = 90;
                y += 60;
            }
        }
    }
}

class FlowerInfoPanel extends JPanel {
    private JLabel name;
    private JTextArea desc;

    public FlowerInfoPanel() {
        setLayout(new BorderLayout());
        name = new JLabel("Water a flower to learn about it");
        desc = new JTextArea(4, 40);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);

        add(name, BorderLayout.NORTH);
        add(desc, BorderLayout.CENTER);
    }

    public void showFlower(Flower f) {
        name.setText(f.getName());
        desc.setText(f.getDescription());
    }

    public void showDone() {
        name.setText("Garden Complete!");
        desc.setText("Now select flowers to build your bouquet.");
    }
}

public class GardenFlowerGUI extends JFrame {
    private GameModel model;
    private GardenPanel garden;
    private BouquetPanel bouquet;
    private FlowerInfoPanel info;

    public GardenFlowerGUI() {
        model = new GameModel();

        setTitle("Flower Garden Project");
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        garden = new GardenPanel(model);
        bouquet = new BouquetPanel(model);
        info = new FlowerInfoPanel();

        JButton waterBtn = new JButton("Water Next Flower");
        JButton addBtn = new JButton("Add to Bouquet");
        JButton clearBtn = new JButton("Clear Bouquet");

        String[] names = model.getFlowers().stream().map(Flower::getName).toArray(String[]::new);
        JList<String> list = new JList<>(names);
        list.setEnabled(false);

        waterBtn.addActionListener(e -> {
            Flower f = model.getCurrentFlower();
            if (f != null) {
                garden.showCan();

                Timer t = new Timer(600, ev -> {
                    model.waterNextFlower();
                    info.showFlower(f);
                    garden.hideCan();
                    garden.repaint();

                    if (model.allFlowersWatered()) {
                        waterBtn.setEnabled(false);
                        list.setEnabled(true);
                        info.showDone();
                    }
                });
                t.setRepeats(false);
                t.start();
            }
        });

        addBtn.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i != -1) {
                model.addToBouquet(model.getFlowers().get(i));
                bouquet.repaint();
            }
        });

        clearBtn.addActionListener(e -> {
            model.clearBouquet();
            bouquet.repaint();
        });

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(list), BorderLayout.CENTER);
        left.add(addBtn, BorderLayout.SOUTH);

        JPanel top = new JPanel();
        top.add(waterBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(info, BorderLayout.CENTER);
        bottom.add(clearBtn, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(garden, BorderLayout.CENTER);
        add(bouquet, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GardenFlowerGUI();
    }
}
