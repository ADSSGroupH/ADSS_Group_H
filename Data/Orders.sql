CREATE TABLE IF NOT EXISTS "Order_Items" (
  orderid   VARCHAR(50) NOT NULL
     REFERENCES "Orders"(orderid),
  item_iid  VARCHAR(50) NOT NULL
     REFERENCES "Items"(iid),
  quantity  INTEGER     NOT NULL,
  PRIMARY KEY(orderid, item_iid)
);