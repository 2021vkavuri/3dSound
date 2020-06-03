from openal import *
import sys
import time
import math

# creates source and listener
source = oalOpen("ping.wav")
listener = oalGetListener()
print(listener.orientation)
#reading input for source position
angle = math.radians(int(sys.argv[2]))
x, y = 0, 0
if sys.argv[1].upper() == 'X':
    x = int(sys.argv[3]) * math.cos(angle)
    y = int(sys.argv[3]) * math.sin(angle)
elif sys.argv[1].upper() == 'Y':
    x = int(sys.argv[3]) * math.sin(angle)
    y = int(sys.argv[3]) * math.cos(angle)

#reading input for listener orientation
if len(sys.argv) > 4:
    langle = math.radians(int(sys.argv[5]))
    lx, ly = 0, 0
    if sys.argv[4].upper() == 'X':
        lx = math.cos(langle)
        ly = math.sin(langle)
    elif sys.argv[4].upper() == 'Y':
        lx = math.sin(langle)
        ly = math.cos(langle)
    #sets the orientation of the listener (which way they are looking)
    print(lx)
    print(ly)

    listener.set_orientation((0, 0, -1, lx, ly, 0))

#sets the position of the source (where the sound is coming from) and the orientation of the listener (which way they are looking)
source.set_position((x, y, 2))


# starts playback
source.play()


# check if the file is still playing
while source.get_state() == AL_PLAYING:
# wait until the file is done playing
    time.sleep(1)

# release resources (don't forget this)
#oalQuit()
