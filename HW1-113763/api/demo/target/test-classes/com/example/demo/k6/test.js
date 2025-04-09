import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// Custom metrics
const errorRate = new Rate('errors');

// Test configuration
export const options = {
  stages: [
    { duration: '10s', target: 5 }, 
    { duration: '15s', target: 100 }, 
    { duration: '20s', target: 30 },  
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'], 
    errors: ['rate<0.1'],              
  },
};

// Initialize variables that will store IDs for testing
let restaurantId = null;
let mealId = null;

export default function() {
  const baseUrl = 'http://localhost:8080/Api';
  const headers = { 'Content-Type': 'application/json' };
  
  // Test Restaurant endpoints
  // =========================
  
  // 1. Create a restaurant
  const restaurantPayload = JSON.stringify({
    name: `Test Restaurant ${Date.now()}`,
    location: 'Test Location'
  });
  
  let createRestaurantResponse = http.post(
    `${baseUrl}/restaurants`, 
    restaurantPayload, 
    { headers }
  );
  
  let createRestaurantCheck = check(createRestaurantResponse, {
    'Create restaurant status is 201': (r) => r.status === 201,
    'Create restaurant returns JSON': (r) => r.headers['Content-Type'].includes('application/json'),
    'Create restaurant returns ID': (r) => r.json('id') !== undefined,
  });
  
  errorRate.add(!createRestaurantCheck);
  
  if (createRestaurantResponse.status === 201) {
    restaurantId = createRestaurantResponse.json('id');
    
    // 2. Get created restaurant
    let getRestaurantResponse = http.get(`${baseUrl}/restaurants/${restaurantId}`);
    
    check(getRestaurantResponse, {
      'Get restaurant status is 200': (r) => r.status === 200,
      'Get restaurant returns correct name': (r) => r.json('name').includes('Test Restaurant'),
    });
    
    // 3. Update restaurant
    const updateRestaurantPayload = JSON.stringify({
      name: `Updated Restaurant ${Date.now()}`,
      location: 'Updated Location'
    });
    
    let updateRestaurantResponse = http.put(
      `${baseUrl}/restaurants/${restaurantId}`, 
      updateRestaurantPayload, 
      { headers }
    );
    
    check(updateRestaurantResponse, {
      'Update restaurant status is 200': (r) => r.status === 200,
      'Update restaurant returns updated name': (r) => r.json('name').includes('Updated Restaurant'),
    });
    
    // 4. Get all restaurants
    let getAllRestaurantsResponse = http.get(`${baseUrl}/restaurants`);
    
    check(getAllRestaurantsResponse, {
      'Get all restaurants status is 200': (r) => r.status === 200,
      'Get all restaurants returns array': (r) => Array.isArray(r.json()),
    });
  }
  
  // Test Meal endpoints
  // ===================
  
  // 5. Create a meal
  if (restaurantId) {
    const now = new Date();
    const mealPayload = JSON.stringify({
      name: `Test Meal ${Date.now()}`,
      descrição: 'Test Description',
      price: 10.99,
      date: now.toISOString().split('T')[0],
      time: now.toTimeString().split(' ')[0],
      numberOfMeals: 10,
      restaurant: {
        id: restaurantId
      }
    });
    
    let createMealResponse = http.post(`${baseUrl}/meals`, mealPayload, { headers });
    
    let createMealCheck = check(createMealResponse, {
      'Create meal status is 201': (r) => r.status === 201,
      'Create meal returns JSON': (r) => r.headers['Content-Type'].includes('application/json'),
      'Create meal returns ID': (r) => r.json('id') !== undefined,
    });
    
    errorRate.add(!createMealCheck);
    
    if (createMealResponse.status === 201) {
      mealId = createMealResponse.json('id');
      
      // 6. Get created meal
      let getMealResponse = http.get(`${baseUrl}/meals/${mealId}`);
      
      check(getMealResponse, {
        'Get meal status is 200': (r) => r.status === 200,
        'Get meal returns correct name': (r) => r.json('name').includes('Test Meal'),
      });
      
      // 7. Update meal
      const updateMealPayload = JSON.stringify({
        name: `Updated Meal ${Date.now()}`,
        descrição: 'Updated Description',
        price: 12.99,
        numberOfMeals: 15
      });
      
      let updateMealResponse = http.put(
        `${baseUrl}/meals/${mealId}`, 
        updateMealPayload, 
        { headers }
      );
      
      check(updateMealResponse, {
        'Update meal status is 200': (r) => r.status === 200,
        'Update meal returns updated name': (r) => r.json('name').includes('Updated Meal'),
        'Update meal returns updated numberOfMeals': (r) => r.json('numberOfMeals') === 15,
      });
      
      // 8. Get all meals
      let getAllMealsResponse = http.get(`${baseUrl}/meals`);
      
      check(getAllMealsResponse, {
        'Get all meals status is 200': (r) => r.status === 200,
        'Get all meals returns array': (r) => Array.isArray(r.json()),
      });
      
      // 9. Get meals for specific restaurant
      let getRestaurantMealsResponse = http.get(`${baseUrl}/restaurants/${restaurantId}/menus`);
      
      check(getRestaurantMealsResponse, {
        'Get restaurant meals status is 200': (r) => r.status === 200,
        'Get restaurant meals returns array': (r) => Array.isArray(r.json()),
      });
      
      // 10. Get reservations for restaurant
      let getRestaurantReservationsResponse = http.get(`${baseUrl}/restaurants/${restaurantId}/reservations`);
      
      check(getRestaurantReservationsResponse, {
        'Get restaurant reservations status is 200': (r) => r.status === 200,
        'Get restaurant reservations returns array': (r) => Array.isArray(r.json()),
      });
    }
  }
  
  // Sleep between iterations to simulate real user behavior
  sleep(1);
}