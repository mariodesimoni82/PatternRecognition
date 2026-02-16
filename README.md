Programming Test - Pattern Recognition
Pattern Recognition
Computer vision involves analyzing patterns in visual images and reconstructing the real world objects that
produced them. The process in often broken up into two phases: feature detection and pattern recognition.
Feature detection involves selecting important features of the image; pattern recognition involves
discovering patterns in the features. We will investigate a particularly clean pattern recognition problem
involving points and line segments. This kind of pattern recognition arises in many other applications, for
example statistical data analysis.

Given a set of N feature points in the plane, determine every line that contains N or more of the points, and
return all points involved. You should also expose a REST API that will allow the caller to:
Add a point to the space
POST /point with body { "x": ..., "y": ... }
Get all points in the space
GET /space
Get all line segments passing through at least N points. Note that a line segment should be a set of
points.
GET /lines/{n}
Remove all points from the space
DELETE /space
