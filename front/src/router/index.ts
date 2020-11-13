import BlankLayout from '@/layouts/BlankLayout.vue';
import Home from '@/views/Home.vue';
import NotFound from '@/views/NotFound.vue';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BlankLayout,
    children: [
      {
        path: '/',
        component: Home,
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
