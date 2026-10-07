# Javadoc output

The generated Javadoc for the chess project lives in this folder. Regenerate it from the project root with:

```bash
javadoc -private -d docs -sourcepath src -subpackages board:pieces:core:ui:utils src/Main.java
```

Then open `docs/index.html` in a browser. Commit the regenerated files so the documentation is available on GitHub.
