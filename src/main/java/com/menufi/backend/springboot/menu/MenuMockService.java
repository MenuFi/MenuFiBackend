package com.menufi.backend.springboot.menu;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuMockService implements MenuService {

    private Map<Integer, MenuItemCollector> menuItemsMap;

    private class MenuItemCollector {

        private Collection<MenuItem> menuItems;
        private int idCounter = 0;

        public MenuItemCollector() {
            this.menuItems = new ArrayList<>();
        }

        public MenuItemCollector(Collection<MenuItem> menuItems) {
            this();
            for (MenuItem menuItem : menuItems) {
                idCounter = Math.max(idCounter, menuItem.id + 1);
                this.menuItems.add(menuItem);
            }

        }

        public MenuItem addMenuItem(AddMenuItemRequest addMenuItemRequest) {
            MenuItem newMenuItem = new MenuItem(
                    idCounter++,
                    addMenuItemRequest.name,
                    "",
                    addMenuItemRequest.description,
                    addMenuItemRequest.price,
                    0);
            menuItems.add(newMenuItem);
            return newMenuItem;
        }

        public Collection<MenuItem> getMenuItems() {
            return menuItems;
        }
    }

    public MenuMockService() {
        menuItemsMap = new HashMap<Integer, MenuItemCollector>();
        menuItemsMap.put(0, new MenuItemCollector(Arrays.asList(
                new MenuItem(0, "Cardiac Arrest Burger", "inet_url", "bad for the heart", 9.99f, 4.5f),
                new MenuItem(1, "Peanut Soup", "inet_url", "soupy", 4.99f, 3.5f))
        ));

        menuItemsMap.put(1, new MenuItemCollector(Arrays.asList(
                new MenuItem(0, "Charred Dogs", "inet_url", "extra crispy", 5.99f, 3.5f),
                new MenuItem(1, "Darlie Cogs", "inet_url", "weird", 14.99f, 2.5f))
        ));

        menuItemsMap.put(2, new MenuItemCollector(Arrays.asList(
                new MenuItem(0, "Panffle", "inet_url", "fluffy", 5.99f, 4.6f),
                new MenuItem(1, "Wacake", "inet_url", "puffy", 7.99f, 3.2f))
        ));
    }

    @Override
    public MenuItem addMenuItem(AddMenuItemRequest addMenuItemRequest) {
        MenuItemCollector menuItemCollector;
        int menuItemsKey = addMenuItemRequest.restaurantId;
        if (menuItemsMap.containsKey(menuItemsKey)) {
            return menuItemsMap.get(menuItemsKey).addMenuItem(addMenuItemRequest);
        }
        return null;
    }

    @Override
    public Collection<MenuItem> getMenuItems(int restaurantId) {
        if (menuItemsMap.containsKey(restaurantId)) {
            return menuItemsMap.get(restaurantId).getMenuItems();
        }
        return Collections.emptyList();
    }
}
