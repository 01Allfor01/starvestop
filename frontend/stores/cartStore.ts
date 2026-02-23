import { create } from 'zustand';
import { CartItem } from '@/types';

interface CartState {
  items: CartItem[];
  totalItems: number;
  totalPrice: number;
  setItems: (items: CartItem[]) => void;
  addItem: (item: CartItem) => void;
  removeItem: (cartId: number) => void;
  updateQuantity: (cartId: number, quantity: number) => void;
  clearCart: () => void;
  calculateTotals: () => void;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  totalItems: 0,
  totalPrice: 0,

  setItems: (items) => {
    set({ items });
    get().calculateTotals();
  },

  addItem: (item) => {
    const existingItem = get().items.find((i) => i.cartId === item.cartId);
    
    if (existingItem) {
      get().updateQuantity(item.cartId, existingItem.quantity + item.quantity);
    } else {
      set((state) => ({ items: [...state.items, item] }));
      get().calculateTotals();
    }
  },

  removeItem: (cartId) => {
    set((state) => ({
      items: state.items.filter((item) => item.cartId !== cartId),
    }));
    get().calculateTotals();
  },

  updateQuantity: (cartId, quantity) => {
    set((state) => ({
      items: state.items.map((item) =>
        item.cartId === cartId ? { ...item, quantity } : item
      ),
    }));
    get().calculateTotals();
  },

  clearCart: () => {
    set({ items: [], totalItems: 0, totalPrice: 0 });
  },

  calculateTotals: () => {
    const items = get().items;
    const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = items.reduce(
      (sum, item) => sum + item.salePrice * item.quantity,
      0
    );
    set({ totalItems, totalPrice });
  },
}));
