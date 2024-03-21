CREATE TABLE cart (
    id INT AUTO_INCREMENT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cart_item (
    cart_id     INT NOT NULL,
    item_id     INT NOT NULL,
    quantity    INT NOT NULL,
    PRIMARY KEY (cart_id, item_id),
    FOREIGN KEY (cart_id) REFERENCES cart(id)
);

INSERT INTO cart () VALUES
  (),
  (),
  ();

INSERT INTO cart_item (cart_id, item_id, quantity) VALUES
  (1, 1, 1),
  (1, 5, 1),
  (2, 2, 1),
  (2, 3, 6),
  (3, 4, 4);
