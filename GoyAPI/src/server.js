import 'dotenv/config';
import app from './app.js';
import { initDb } from './config/db.js';

const PORT = process.env.PORT;

await initDb();

app.listen(PORT, () => {
  console.log(`GoyAPI escuchando en http://localhost:${PORT}`);
});
