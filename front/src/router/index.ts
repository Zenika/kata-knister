import BlankLayout from '@/layouts/BlankLayout.vue';
import { RoomModel } from '@/models/Room';
import store from '@/store';
import Home from '@/views/Home.vue';
import NotFound from '@/views/NotFound.vue';
import Room from '@/views/Room.vue';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BlankLayout,
    children: [
      {
        name: 'Home',
        path: '/',
        component: Home,
      },
      {
        path: '/room',
        component: Room,
        beforeEnter: (_, __, next) => {
          // TODO: faire plsu propre
          const hasRoom = !!((store?.state?.room as unknown) as RoomModel)?._id;

          if (!hasRoom) {
            next({ name: 'Home' });
            return;
          }

          next();
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
