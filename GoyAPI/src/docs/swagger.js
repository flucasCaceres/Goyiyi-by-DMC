// src/docs/swagger.js
import swaggerJSDoc from 'swagger-jsdoc';
import { fileURLToPath } from 'url';
import path from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const options = {
  definition: {
    openapi: '3.0.0',
    info: { title: 'GoyAPI', version: '1.0.0', description: 'API REST de eventos (Goya)' },
    servers: [{ url: 'http://localhost:3000', description: 'Dev' }],
    components: {
      securitySchemes: {
        bearerAuth: { type: 'http', scheme: 'bearer', bearerFormat: 'JWT' }
      }
    }
  },
  apis: [
    path.join(__dirname, '..', 'routes', '**', '*.js'),
    path.join(__dirname, '..', 'app.js'),
  ],
};

export const swaggerSpec = swaggerJSDoc(options);
export default swaggerSpec;