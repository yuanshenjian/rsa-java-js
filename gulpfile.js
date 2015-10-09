var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var paths = {
    scripts: ['js/index.js','js/main.js']
};
gulp.task('clean',function(){

});

gulp.task('default', function () {
    gulp.src(paths.scripts).pipe(uglify()).pipe(concat('all.min.js')).pipe(gulp.dest('dest'));
});