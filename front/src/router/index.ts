import BlankLayout from '@/layouts/BlankLayout.vue';
import store from '@/store';
import Home from '@/views/Home.vue';
import NotFound from '@/views/NotFound.vue';
import Room from '@/views/Room.vue';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

function resetState() {
  store.state.playerName = '';
  store.state.room.id = '';
}

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BlankLayout,
    children: [
      {
        name: 'Home',
        path: '/',
        component: Home,
        beforeEnter: (_, __, next) => {
          const hasRoom = !!store.state.room.id;

          if (hasRoom) {
            next({ name: 'Room' });
            return;
          }

          next();
        },
      },
      {
        name: 'Room',
        path: '/room',
        component: Room,
        beforeEnter: (_, __, next) => {
          const hasRoom = !!store.state.room.id;

          if (!hasRoom) {
            next({ name: 'Home' });
            return;
          }

          next();
        },
      },
      {
        name: 'Reset',
        path: '/reset',
        component: Home,
        beforeEnter: (_, __, next) => {
          resetState();
          next({ name: 'Home' });
        },
      },
    ],
  },
  {
    path: '/:catchAll(.*)',
    component: NotFound,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach(to => {
  const nearestWithTitle = to.matched
    .slice()
    .reverse()
    .find(r => r.meta && r.meta.title);

  if (nearestWithTitle) {
    document.title = nearestWithTitle.meta.title;
  }
});

export default router;

