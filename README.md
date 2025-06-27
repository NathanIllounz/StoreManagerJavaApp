# 🧾 StoreManagerJavaApp

Java client-server inventory management system with algorithmic recommendations.  
This project was built as part of a final course assignment.

---

## 📦 Features

- Manage product inventory (add, delete, update, view)
- Track income & expenses
- Recommend what products to reorder
- Recommend products with highest profit margins
- GUI using JavaFX
- Server-Client communication using TCP Sockets
- JSON-based request/response protocol
- Layered architecture with DAO, Service, Controller

---

## 🧠 Project Structure

| Path | File | Type | Purpose |
|------|------|------|---------|
| `algorithm/interfaces/` | `IProductRecommendation.java` | Interface | Contract for recommendation logic |
| `algorithm/impl/` | `LowStockRecommendation.java` | Class | Recommends products low in stock |
| `algorithm/impl/` | `HighProfitRecommendation.java` | Class | Recommends high-profit products |
| `model/` | `Product.java`, `FinanceRecord.java` | Class | Data models |
| `dao/` | `IDao.java` | Interface | Data access contract |
| `dao/` | `FileDaoImpl.java` | Class | File-based data storage |
| `service/` | `InventoryService.java` | Class | Business logic |
| `server/` | `Server.java`, `HandleRequest.java` | Class | TCP server components |
| `client/ui/` | `ClientApp.java` | Class | JavaFX UI |
| `client/socket/` | `ClientSocket.java` | Class | TCP client communication |
| `controller/` | `InventoryController.java` | Class | Connects request to logic |
| `network/` | `Request.java`, `Response.java` | Class | JSON request/response |
| `test/` | `ServiceTest.java`, `RecommendationTest.java` | Class | Unit tests |

---

## 🔧 Technologies Used

- Java 11+
- JavaFX
- Sockets (TCP)
- Gson (for JSON)
- JUnit 5

---

## 🧪 Still in Development

This is a work in progress!  
More features, algorithms, and GUI functionality are being added step by step.

