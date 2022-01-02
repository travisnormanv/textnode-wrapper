# textnode-wrapper
Wrap text nodes based on text string positions from an HTML string.

This project is powered by **Jsoup**.

## Example usage

Html String:
```html
<div>Hello, World!</div>
```
For this example we will wrap element to `Hello` and `World` respectively.
We will now create `Position` object with respective start position and end position.

```java
Position helloPos = new Position(0, 4); // Hello
Position worldPos = new Position(7, 11); // World
```

Pass the `htmlString` as `String` and `Position` objects as an `ArrayList` to `Wrapper`.

```java
String htmlString = "<div>Hello, World!</div>";
ArrayList<Position> positions = new ArrayList<Position>();
positions.add(helloPos);
positions.add(worldPos);

Wrapper wrapper = new Wrapper();
wrapper.wrapTextStrings(htmlString, positions);
```

Return `String` will be:

```html
<div><wrapper id="wrapperId0">Hello</wrapper>, <wrapper id="wrapperID1">World</wrapper>!</div>
```

Wrapper element can be set in a `properties` file:
```properties
wrapper=*insert-your-element-tag-here*
```

Default wrapper element tag is `wrapper`.