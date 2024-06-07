import os
import re

# Define the array of styles
styles = [
    "black",
    "darkBlue",
    "darkGreen",
    "darkAqua",
    "darkRed",
    "darkPurple",
    "gold",
    "gray",
    "darkGray",
    "blue",
    "green",
    "aqua",
    "red",
    "lightPurple",
    "yellow",
    "white",
    "obfuscated",
    "bold",
    "strikethrough",
    "underline",
    "italic",
    "reset"
]

# Recursively find all .kt files under src/main/kotlin
for root, _, files in os.walk("src/main/kotlin"):
    for file in files:
        if file.endswith(".kt"):
            file_path = os.path.join(root, file)
            with open(file_path, 'r') as f:
                file_content = f.read()

            for style in styles:
                # Create the regex pattern for the current style
                pattern = rf"(LangKeys\.[A-Za-z0-9_]+)\.{style}\(\)"
                # Replace the pattern in the file content
                file_content = re.sub(pattern, r"\1.get()." + style + "()", file_content)

            with open(file_path, 'w') as f:
                f.write(file_content)
