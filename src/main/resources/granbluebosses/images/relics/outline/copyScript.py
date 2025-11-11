import os
import shutil

# Loop through all files in the current directory
for filename in os.listdir('.'):
    if filename.endswith('.png') and '_' in filename:
        # Split filename and remove the extension
        base_name = filename[:-4]  # remove ".png"
        
        # Split words by underscore and capitalize each
        parts = base_name.split('_')
        new_name = ''.join(word.capitalize() for word in parts) + '.png'
        
        # Copy the file with the new name
        shutil.copy(filename, new_name)
        print(f'Created copy: {new_name}')